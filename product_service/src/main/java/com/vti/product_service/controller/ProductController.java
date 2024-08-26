package com.vti.product_service.controller;

import com.vti.product_service.dto.ProductDto;
import com.vti.product_service.dto.ProductImageDto;
import com.vti.product_service.entity.Product;
import com.vti.product_service.entity.ProductImage;
import com.vti.product_service.exception.DataNotFoundException;
import com.vti.product_service.form.CreatingProductForm;
import com.vti.product_service.form.ProductFilterForm;
import com.vti.product_service.service.IProductImageService;
import com.vti.product_service.service.IProductService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@RestController
@RequestMapping("/api/v1/products")
@Validated
public class ProductController {
    @Autowired
    private IProductService productService;
    @Autowired
    private IProductImageService productImageService;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public Page<ProductDto> getAllProducts(@PageableDefault(page = 0, size = 5) Pageable pageable,
            ProductFilterForm form) {
        Page<Product> productPage = productService.getAllProducts(pageable, form);
        List<ProductDto> productDtoList = modelMapper.map(productPage.getContent(), new TypeToken<List<ProductDto>>() {
        }.getType());
        Page<ProductDto> productDtoPage = new PageImpl<>(productDtoList, pageable, productPage.getTotalElements());
        return productDtoPage;
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@Valid @RequestBody CreatingProductForm form) {
        Product productNew = productService.createProduct(form);
        return new ResponseEntity<>("CREATED", HttpStatus.CREATED);
    }

    @PostMapping(value = "uploads/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages(@PathVariable int id, @ModelAttribute("files") List<MultipartFile> files)
            throws DataNotFoundException, IOException {
        try {

            Product existingProduct = productService.getProductById(id);
            files = files == null ? new ArrayList<MultipartFile>() : files;
            if (files.size() > ProductImage.Maximum_Images_Per_Product) {
                return ResponseEntity.badRequest().body(
                        "You are only allowed to upload image files under" + ProductImage.Maximum_Images_Per_Product);
            }
            List<ProductImage> listProductImages = new ArrayList<>();
            for (MultipartFile file : files) {
                if (file.getSize() == 0) {
                    continue;
                }
                if (file.getSize() > 30 * 1024 * 1024) {
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                            .body("File is so large!(Maxium =30MB)");
                }
                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                            .body("File isn't an image");
                }
                String fileName = storeFile(file);
                ProductImage productImage = productService.createProductImage(
                        existingProduct.getId(),
                        ProductImageDto.builder()
                                .imageUrl(fileName)
                                .build());
                listProductImages.add(productImage);
            }

            return ResponseEntity.ok().body(listProductImages);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

    public String storeFile(MultipartFile file) throws IOException {
        if (!isImageFile(file) || file.getOriginalFilename() == null) {
            throw new IOException("Invalid Type ImageFile");
        }
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String uniqueFilename = UUID.randomUUID().toString() + "_" + fileName;
        Path path = Paths.get("Ecommerce_SellingPhone_Web_Images");
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
        Path destination = Paths.get(path.toString(), uniqueFilename);
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFilename;
    }

    @GetMapping("{id}")
    public ProductDto getProductById(@PathVariable int id) throws DataNotFoundException {
        Product product = productService.getProductById(id);
        return modelMapper.map(product, ProductDto.class);
    }

    @PutMapping("{id}")
    public ResponseEntity<String> updateProduct(@PathVariable int id, @RequestBody CreatingProductForm form)
            throws DataNotFoundException {
        productService.updateProduct(id, form);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @PutMapping("uploads/{id}/image/{imageId}")
    public ResponseEntity<?> updateImages(@PathVariable int id, @PathVariable(name = "imageId") int imageId,
            @ModelAttribute("file") MultipartFile file) throws Exception {
        try {

            Product existingProduct = productService.getProductById(id);
            ProductImage existingImage = productImageService.getProductImageById(imageId);
            if (existingImage == null || existingImage.getProduct().getId() != id) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Hình ảnh không tồn tại hoặc không thuộc về sản phẩm này");
            }
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Tệp tin rỗng");
            }
            if (!isImageFile(file)) {
                return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("Tệp tin không phải là hình ảnh");
            }

            if (file.getSize() > 30 * 1024 * 1024) {
                return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                        .body("Tệp tin quá lớn! (Tối đa = 30MB)");
            }
            // Lưu tệp tin mới và lấy tên tệp tin
            String newFileName = storeFile(file);
            // Cập nhật URL của hình ảnh
            existingImage.setImageUrl(newFileName);
            productImageService.updateProductImage(existingImage);

            // Tùy chọn, xóa tệp tin hình ảnh cũ từ máy chủ nếu cần

            return ResponseEntity.ok().body(existingImage);
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi lưu tệp tin");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable int id) throws DataNotFoundException {
        productService.deleteProduct(id);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}
