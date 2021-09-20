package models;

import lombok.Data;

import java.util.List;

@Data
public class Order {
    String id;
    List<Item> items;
}
