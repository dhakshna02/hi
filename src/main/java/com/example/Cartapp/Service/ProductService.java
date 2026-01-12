package com.example.Cartapp.Service;


import com.example.Cartapp.Model.Product;
import com.example.Cartapp.Repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
@Service
public class ProductService {

   @Autowired
    private ProductRepo repo;

    public List<Product> getProduct(){
        return repo.findAll();

    }

    public List<Product> getProductsForHome(String city) {
        return repo.findByCitySafe(city);
    }



    public Product getProductById(int id)  {
        return repo.findById( id).orElse(null);
    }
    public List<Product> getProductsByCityAndCategory(String city, String category) {
        return repo.findByCityIgnoreCaseAndCategoryIgnoreCase(city, category);
    }





    public Product addProduct(Product product , MultipartFile imageFile) throws IOException {
         product.setImageName(imageFile.getOriginalFilename());
         product.setImageType(imageFile.getContentType());
         product.setImageData(imageFile.getBytes());
          return repo.save(product);
    }

    public Product updateProduct(int id, Product product, MultipartFile imageFile) throws IOException {
     product.setImageName(imageFile.getOriginalFilename());
     product.setImageType(imageFile.getContentType());
     product.setImageData(imageFile.getBytes());
     return repo.save(product);
    }

    public void deleteProduct(int id)  {

        repo.deleteById(id);
    }




    public List<Product> searchProduct(String keyword) {
        return repo.searchProduct(keyword);

    }

}
