package com.example.Cartapp.Controller;

import com.example.Cartapp.Model.Product;
import com.example.Cartapp.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("api/")
public class ProductController {

    @Autowired
    private ProductService service;

@GetMapping("/")
    public String home(){
    return " Hi  Welcome to Home Page";
}


@GetMapping ("/products")
public List<Product> getProduct(){
    return service.getProduct();
}

//    @GetMapping("/products/home")
//    public ResponseEntity<List<Product>> getHomeProducts(
//            @RequestParam String city) {
//
//        String cleanCity = city.trim();
//        List<Product> products = service.getProductsForHome(cleanCity);
//        return ResponseEntity.ok(products);
//    }






    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable  int id){
    Product pro = service.getProductById(id);
    if(pro != null)
        return new ResponseEntity<>(pro, HttpStatus.OK);
    else
     return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

 @PostMapping("/product")
    public ResponseEntity<?>addProduct(@RequestPart Product product,
                                       @RequestPart MultipartFile imageFile) throws IOException {


    try {
        System.out.println(imageFile.getSize());
        Product pr1 = service.addProduct(product, imageFile);
        return new ResponseEntity<>( pr1,HttpStatus.CREATED);
    }
    catch( Exception e ){

        return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
     }
 }


    @GetMapping("/products/home")
    public ResponseEntity<List<Product>> getHomeProducts(
            @RequestParam String city,
            @RequestParam(required = false) String category
    ) {

        System.out.println("CITY = " + city);
        System.out.println("CATEGORY = " + category);

        List<Product> products;

        if (category != null && !category.trim().isEmpty()) {
            products = service.getProductsByCityAndCategory(city.trim(), category.trim());
        } else {
            products = service.getProductsForHome(city.trim());
        }

        return ResponseEntity.ok(products);
    }


    @GetMapping("/product/{productId}/image")
 public ResponseEntity<byte[]> getImage(@PathVariable int productId){

    Product product = service.getProductById(productId);
    byte[] imageFile= product.getImageData();
    return ResponseEntity.ok().contentType(MediaType.valueOf(product.getImageType())).body(imageFile);
    }

    @PutMapping("/product/{id}")
        public ResponseEntity<String>updateProduct(@PathVariable int id,@RequestPart Product product,@RequestPart MultipartFile imageFile)  {
            Product produ1 =  null;
        try {
            produ1 = service.updateProduct(id,product,imageFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (produ1 != null){
               return new ResponseEntity<>("updated",HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>("notupdated",HttpStatus.BAD_REQUEST);
            }
        }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<String>deleteProduct(@PathVariable int id ){
    Product dprod = service.getProductById(id);
    if(dprod != null ){
        service.deleteProduct(id);
        return new ResponseEntity<>("deleted",HttpStatus.OK);
    }
    else{return new ResponseEntity<>("not deleted",HttpStatus.BAD_REQUEST);}
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<Product>>searchProduct(@RequestParam String keyword){
        System.out.println( "Searching with "+ keyword);
    List<Product> produc = service.searchProduct(keyword);
    return new ResponseEntity<>(produc,HttpStatus.OK);
    }




}
