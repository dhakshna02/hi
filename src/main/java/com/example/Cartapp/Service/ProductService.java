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
//     product.setImageName(imageFile.getOriginalFilename());
//     product.setImageType(imageFile.getContentType());
//     product.setImageData(imageFile.getBytes());
//     return repo.save(product);



            Product existing = repo.findById(id)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            // 🔹 STOCK LOGIC (IMPORTANT)
            int updatedStock = product.getStockQuantity();

            // 🔥 DELETE PRODUCT IF STOCK = 0
            if (updatedStock <= 0) {
                repo.deleteById(id);
                return existing;
            }

            // 🔹 UPDATE FIELDS
            existing.setName(product.getName());
            existing.setBrand(product.getBrand());
            existing.setDescription(product.getDescription());
            existing.setPrice(product.getPrice());
            existing.setCategory(product.getCategory());
            existing.setReleaseDate(product.getReleaseDate());
            existing.setCity(product.getCity());

            existing.setStockQuantity(updatedStock);
            existing.setProductAvailable(true);

            // 🔹 IMAGE (KEEP SAME IMAGE IF NOT CHANGED)
            if (imageFile != null && !imageFile.isEmpty()) {
                existing.setImageName(imageFile.getOriginalFilename());
                existing.setImageType(imageFile.getContentType());
                existing.setImageData(imageFile.getBytes());
            }

            return repo.save(existing);
        }



    public void deleteProduct(int id)  {

        repo.deleteById(id);
    }




    public List<Product> searchProduct(String keyword) {
        return repo.searchProduct(keyword);

    }

    public void reduceStock(int id, int quantity) {
        Product product = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getStockQuantity() < quantity) {
            throw new RuntimeException("Not enough stock");
        }

        int updatedStock = product.getStockQuantity() - quantity;
        product.setStockQuantity(updatedStock);

        if (updatedStock == 0) {
            product.setProductAvailable(false);
        }

        repo.save(product);
    }


}
