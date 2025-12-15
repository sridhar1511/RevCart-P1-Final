@echo off
echo Populating RevCart Database with Products...

curl -X POST "http://localhost:8081/api/products" ^
-H "Content-Type: application/json" ^
-d "{\"name\":\"Fresh Apples\",\"category\":\"fruits\",\"price\":\"120\",\"unit\":\"1kg\",\"image\":\"https://images.unsplash.com/photo-1560806887-1e4cd0b6cbd6?w=300&h=200&fit=crop\",\"description\":\"Fresh red apples\",\"stockQuantity\":100}"

curl -X POST "http://localhost:8081/api/products" ^
-H "Content-Type: application/json" ^
-d "{\"name\":\"Bananas\",\"category\":\"fruits\",\"price\":\"60\",\"unit\":\"1kg\",\"image\":\"https://images.unsplash.com/photo-1571771894821-ce9b6c11b08e?w=300&h=200&fit=crop\",\"description\":\"Ripe yellow bananas\",\"stockQuantity\":100}"

curl -X POST "http://localhost:8081/api/products" ^
-H "Content-Type: application/json" ^
-d "{\"name\":\"Oranges\",\"category\":\"fruits\",\"price\":\"100\",\"unit\":\"1kg\",\"image\":\"https://images.unsplash.com/photo-1547514701-42782101795e?w=300&h=200&fit=crop\",\"description\":\"Juicy oranges\",\"stockQuantity\":100}"

curl -X POST "http://localhost:8081/api/products" ^
-H "Content-Type: application/json" ^
-d "{\"name\":\"Carrots\",\"category\":\"vegetables\",\"price\":\"40\",\"unit\":\"500g\",\"image\":\"https://images.unsplash.com/photo-1445282768818-728615cc910a?w=300&h=200&fit=crop\",\"description\":\"Fresh orange carrots\",\"stockQuantity\":100}"

curl -X POST "http://localhost:8081/api/products" ^
-H "Content-Type: application/json" ^
-d "{\"name\":\"Tomatoes\",\"category\":\"vegetables\",\"price\":\"80\",\"unit\":\"1kg\",\"image\":\"https://images.unsplash.com/photo-1592924357228-91a4daadcfea?w=300&h=200&fit=crop\",\"description\":\"Fresh red tomatoes\",\"stockQuantity\":100}"

curl -X POST "http://localhost:8081/api/products" ^
-H "Content-Type: application/json" ^
-d "{\"name\":\"Milk\",\"category\":\"dairy\",\"price\":\"55\",\"unit\":\"1L\",\"image\":\"https://nutritionsource.hsph.harvard.edu/wp-content/uploads/2024/11/AdobeStock_354060824-1024x683.jpeg\",\"description\":\"Fresh cow milk\",\"stockQuantity\":100}"

curl -X POST "http://localhost:8081/api/products" ^
-H "Content-Type: application/json" ^
-d "{\"name\":\"Bread\",\"category\":\"bakery\",\"price\":\"35\",\"unit\":\"1 loaf\",\"image\":\"https://assets.bonappetit.com/photos/5c62e4a3e81bbf522a9579ce/1:1/pass/milk-bread.jpg\",\"description\":\"Whole wheat bread\",\"stockQuantity\":100}"

curl -X POST "http://localhost:8081/api/products" ^
-H "Content-Type: application/json" ^
-d "{\"name\":\"Smartphone\",\"category\":\"electronics\",\"price\":\"15000\",\"unit\":\"1 piece\",\"image\":\"https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=300&h=200&fit=crop\",\"description\":\"Latest smartphone\",\"stockQuantity\":50}"

echo Products added successfully!
pause