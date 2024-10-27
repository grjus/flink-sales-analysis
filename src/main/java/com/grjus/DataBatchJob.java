package com.grjus;


import com.grjus.config.AppConfig;
import com.grjus.config.ConfigDto;
import com.grjus.dtos.CategorySalesDto;
import com.grjus.entities.OrderItem;
import com.grjus.entities.Product;
import com.grjus.entities.ProductOrderItemDto;
import com.grjus.output.GenericFileOutput;
import org.apache.flink.api.common.functions.JoinFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.operators.Order;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;


public class DataBatchJob {
    public static void main(String[] args) throws Exception {
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        AppConfig appConfig = new AppConfig();
        ConfigDto config = appConfig.getConfig();
        System.out.println("Products file path: " + config.getProductsFilePath());

        DataSource<OrderItem> orderItems = env.readCsvFile(config.getOrdersFilePath())
                .ignoreFirstLine()
                .pojoType(OrderItem.class,
                        "orderItemId", "orderId", "productId", "quantity", "pricePerUnit");

        DataSource<Product> products = env.readCsvFile(config.getProductsFilePath())
                .ignoreFirstLine()
                .pojoType(Product.class, "productId", "name", "description", "price", "category");


        DataSet<ProductOrderItemDto> joined = orderItems.join(products)
                .where("productId")
                .equalTo("productId")
                .with((JoinFunction<OrderItem, Product, ProductOrderItemDto>) (first, second) -> new ProductOrderItemDto(
                        second.getProductId().toString(),
                        second.getName(),
                        first.getPricePerUnit(),
                        first.getQuantity(),
                        first.getPricePerUnit() * first.getQuantity(),
                        second.getCategory(
                        )
                )).returns(ProductOrderItemDto.class);

        // group by category to get the sales and count
        DataSet<CategorySalesDto> categorySalesDataSet = joined.map((MapFunction<ProductOrderItemDto, CategorySalesDto>) record ->
                        new CategorySalesDto(record.getCategory(), record.getTotalPrice(), 1))
                .returns(CategorySalesDto.class)
                .groupBy("category")
                .reduce((ReduceFunction<CategorySalesDto>) (value1, value2) ->
                        new CategorySalesDto(value1.getCategory(), value1.getTotalSales() + value2.getTotalSales(), value1.getCount() + value2.getCount()));
        categorySalesDataSet.sortPartition("totalSales", Order.DESCENDING);
        categorySalesDataSet.output(new GenericFileOutput<CategorySalesDto>(config.getOutputFilePath()));

        env.execute("Flink Sales Analysis");


    }
}