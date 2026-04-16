package nodomain.cloudfide.task.mappers;

import lombok.RequiredArgsConstructor;
import nodomain.cloudfide.task.dto.request.CreateProductRequest;
import nodomain.cloudfide.task.dto.request.UpdateProductRequest;
import nodomain.cloudfide.task.dto.response.ProductResponse;
import nodomain.cloudfide.task.entity.Producer;
import nodomain.cloudfide.task.entity.Product;
import nodomain.cloudfide.task.entity.ProductAttribute;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductMapper {

    public Product toEntity(CreateProductRequest request, Producer producer) {
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setProducer(producer);

        if (request.getAttributes() != null) {
            request.getAttributes().forEach((key, value) -> {
                ProductAttribute attribute = new ProductAttribute();
                attribute.setProduct(product);
                attribute.setKey(key);
                attribute.setValue(value);
                product.getAttributes().add(attribute);
            });
        }

        return product;
    }

    public void updateEntity(Product product, UpdateProductRequest request, Producer producer) {
        if (request.getName() != null)
            product.setName(request.getName());

        if (request.getDescription() != null)
            product.setDescription(request.getDescription());

        if (request.getPrice() != null)
            product.setPrice(request.getPrice());

        if (producer != null)
            product.setProducer(producer);

        if (request.getAttributes() != null) {
            // remove all
            product.getAttributes().clear();

            // and add new ones
            request.getAttributes().forEach((key, value) -> {
                ProductAttribute attribute = new ProductAttribute();
                attribute.setProduct(product);
                attribute.setKey(key);
                attribute.setValue(value);
                product.getAttributes().add(attribute);
            });
        }
    }

    public ProductResponse toResponse(Product product) {
        Map<String, String> attributes = product.getAttributes().stream()
            .collect(Collectors.toMap(
                ProductAttribute::getKey,
                ProductAttribute::getValue,
                (v1, v2) -> v1,
                HashMap::new
            ));

        return new ProductResponse(
            product.getId(),
            product.getName(),
            product.getDescription(),
            product.getPrice(),
            product.getProducer().getId(),
            attributes,
            product.getCreatedAt(),
            product.getUpdatedAt()
        );
    }
}