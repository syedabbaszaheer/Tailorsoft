package models;
import lombok.Data;
import java.util.List;

@Data
public class SampleResponse {
    List<Product> products;
    List<Order> orders;
}
