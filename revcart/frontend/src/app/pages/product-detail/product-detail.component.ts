import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { CartService } from '../../services/cart.service';
import { WishlistService } from '../../services/wishlist.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-product-detail',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './product-detail.component.html',
  styleUrls: ['./product-detail.component.scss']
})
export class ProductDetailComponent implements OnInit {
  product: any;
  relatedProducts: any[] = [];
  quantity = 1;
  selectedSize: string = '';
  availableSizes: string[] = [];

  constructor(
    private route: ActivatedRoute,
    private cartService: CartService,
    public wishlistService: WishlistService,
    public authService: AuthService
  ) {}

  ngOnInit() {
    const id = this.route.snapshot.params['id'];
    this.loadProduct(id);
  }

  getSizesForProduct(product: any): string[] {
    if (product.category.includes('clothing')) {
      if (product.name.toLowerCase().includes('pant') || product.name.toLowerCase().includes('jeans')) {
        return ['28', '30', '32', '34', '36', '38', '40'];
      } else if (product.name.toLowerCase().includes('shirt') || product.name.toLowerCase().includes('t-shirt') || product.name.toLowerCase().includes('dress') || product.name.toLowerCase().includes('kurti')) {
        return ['XS', 'S', 'M', 'L', 'XL', 'XXL'];
      } else if (product.name.toLowerCase().includes('shoe')) {
        return ['5', '6', '7', '8', '9', '10', '11', '12'];
      }
    }
    return [];
  }

  loadProduct(id: string) {
    this.setProductAndSizes(id);
  }

  setProductAndSizes(id: string) {
    const products = [
      // Fruits
      { id: 1, name: 'Fresh Apples', category: 'fruits', price: 120, unit: '1kg', image: 'https://images.unsplash.com/photo-1560806887-1e4cd0b6cbd6?w=500&h=500&fit=crop', description: 'Fresh red apples' },
      { id: 2, name: 'Bananas', category: 'fruits', price: 60, unit: '1kg', image: 'https://images.unsplash.com/photo-1571771894821-ce9b6c11b08e?w=500&h=500&fit=crop', description: 'Ripe yellow bananas' },
      { id: 3, name: 'Oranges', category: 'fruits', price: 100, unit: '1kg', image: 'https://images.unsplash.com/photo-1547514701-42782101795e?w=500&h=500&fit=crop', description: 'Juicy oranges' },
      { id: 4, name: 'Grapes', category: 'fruits', price: 150, unit: '500g', image: 'https://images.unsplash.com/photo-1537640538966-79f369143f8f?w=500&h=500&fit=crop', description: 'Sweet grapes' },
      { id: 37, name: 'Strawberries', category: 'fruits', price: 200, unit: '250g', image: 'https://images.unsplash.com/photo-1518635017498-87f514b751ba?w=300&h=200&fit=crop', description: 'Fresh strawberries' },
      { id: 38, name: 'Mangoes', category: 'fruits', price: 180, unit: '1kg', image: 'https://www.metropolisindia.com/upgrade/blog/upload/25/05/benefits-of-mangoes1747828357.webp', description: 'Sweet mangoes' },
      { id: 39, name: 'Pineapple', category: 'fruits', price: 80, unit: '1 piece', image: 'https://images.unsplash.com/photo-1589820296156-2454bb8a6ad1?w=300&h=200&fit=crop', description: 'Fresh pineapple' },
      { id: 40, name: 'Watermelon', category: 'fruits', price: 40, unit: '1kg', image: 'https://images.unsplash.com/photo-1587049352846-4a222e784d38?w=300&h=200&fit=crop', description: 'Juicy watermelon' },
      
      // Vegetables
      { id: 5, name: 'Carrots', category: 'vegetables', price: 40, unit: '500g', image: 'https://images.unsplash.com/photo-1445282768818-728615cc910a?w=300&h=200&fit=crop', description: 'Fresh orange carrots' },
      { id: 6, name: 'Tomatoes', category: 'vegetables', price: 80, unit: '1kg', image: 'https://images.unsplash.com/photo-1592924357228-91a4daadcfea?w=300&h=200&fit=crop', description: 'Fresh red tomatoes' },
      { id: 7, name: 'Onions', category: 'vegetables', price: 30, unit: '1kg', image: 'https://images.unsplash.com/photo-1508747703725-719777637510?w=300&h=200&fit=crop', description: 'Fresh onions' },
      { id: 8, name: 'Potatoes', category: 'vegetables', price: 25, unit: '1kg', image: 'https://images.unsplash.com/photo-1518977676601-b53f82aba655?w=300&h=200&fit=crop', description: 'Fresh potatoes' },
      { id: 33, name: 'Broccoli', category: 'vegetables', price: 60, unit: '500g', image: 'https://images.unsplash.com/photo-1628773822503-930a7eaecf80?w=300&h=200&fit=crop', description: 'Fresh broccoli' },
      { id: 34, name: 'Spinach', category: 'vegetables', price: 35, unit: '250g', image: 'https://images.unsplash.com/photo-1576045057995-568f588f82fb?w=300&h=200&fit=crop', description: 'Fresh spinach leaves' },
      { id: 35, name: 'Bell Peppers', category: 'vegetables', price: 70, unit: '500g', image: 'https://images.unsplash.com/photo-1563565375-f3fdfdbefa83?w=300&h=200&fit=crop', description: 'Colorful bell peppers' },
      { id: 36, name: 'Cauliflower', category: 'vegetables', price: 45, unit: '1 piece', image: 'https://images.unsplash.com/photo-1594282486552-05b4d80fbb9f?w=300&h=200&fit=crop', description: 'Fresh cauliflower' },
      
      // Dairy
      { id: 9, name: 'Milk', category: 'dairy', price: 55, unit: '1L', image: 'https://nutritionsource.hsph.harvard.edu/wp-content/uploads/2024/11/AdobeStock_354060824-1024x683.jpeg', description: 'Fresh cow milk' },
      { id: 10, name: 'Cheese', category: 'dairy', price: 200, unit: '250g', image: 'https://images.unsplash.com/photo-1486297678162-eb2a19b0a32d?w=300&h=200&fit=crop', description: 'Fresh cheese' },
      { id: 11, name: 'Yogurt', category: 'dairy', price: 45, unit: '500g', image: 'https://img.freepik.com/free-vector/realistic-vector-icon-illustration-strawberry-yoghurt-jar-with-spoon-full-yogurt-isolated_134830-2521.jpg?semt=ais_hybrid&w=740&q=80', description: 'Greek yogurt' },
      { id: 12, name: 'Butter', category: 'dairy', price: 120, unit: '200g', image: 'https://images.unsplash.com/photo-1589985270826-4b7bb135bc9d?w=300&h=200&fit=crop', description: 'Fresh butter' },
      { id: 57, name: 'Cream', category: 'dairy', price: 80, unit: '200ml', image: 'https://www.realsimple.com/thmb/WIQw_c6ePyPKkXAGrFVB5hvMN_A=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/make-sour-cream-2000-513d49b3aaba4708a67b19380cc32de6.jpg', description: 'Heavy cream' },
      { id: 58, name: 'Ice Cream', category: 'dairy', price: 150, unit: '500ml', image: 'https://images.unsplash.com/photo-1567206563064-6f60f40a2b57?w=300&h=200&fit=crop', description: 'Vanilla ice cream' },
      { id: 59, name: 'Paneer', category: 'dairy', price: 180, unit: '250g', image: 'https://chennaionlineshopping.in/image/cache/catalog/Products/panner/amul%20panner-800x800.jpg', description: 'Fresh paneer' },
      { id: 60, name: 'Ghee', category: 'dairy', price: 300, unit: '500ml', image: 'https://ueirorganic.com/cdn/shop/files/purecowghee.jpg?v=1689066451', description: 'Pure cow ghee' },
      
      // Bakery
      { id: 13, name: 'Bread', category: 'bakery', price: 35, unit: '1 loaf', image: 'https://assets.bonappetit.com/photos/5c62e4a3e81bbf522a9579ce/1:1/pass/milk-bread.jpg', description: 'Whole wheat bread' },
      { id: 14, name: 'Croissant', category: 'bakery', price: 25, unit: '1 piece', image: 'https://sugargeekshow.com/wp-content/uploads/2022/11/croissants_featured.jpg', description: 'Buttery croissant' },
      { id: 15, name: 'Muffins', category: 'bakery', price: 80, unit: '4 pieces', image: 'https://images.unsplash.com/photo-1607958996333-41aef7caefaa?w=300&h=200&fit=crop', description: 'Blueberry muffins' },
      { id: 16, name: 'Cookies', category: 'bakery', price: 60, unit: '6 pieces', image: 'https://images.unsplash.com/photo-1499636136210-6f4ee915583e?w=300&h=200&fit=crop', description: 'Chocolate cookies' },
      { id: 61, name: 'Donuts', category: 'bakery', price: 120, unit: '6 pieces', image: 'https://images.unsplash.com/photo-1551024506-0bccd828d307?w=300&h=200&fit=crop', description: 'Glazed donuts' },
      { id: 62, name: 'Cake', category: 'bakery', price: 400, unit: '1 piece', image: 'https://images.unsplash.com/photo-1578985545062-69928b1d9587?w=300&h=200&fit=crop', description: 'Chocolate cake' },
      { id: 63, name: 'Bagels', category: 'bakery', price: 90, unit: '4 pieces', image: 'https://www.tasteofhome.com/wp-content/uploads/2025/01/Homemade-Bagels_EXPS_TOHD25_15702_ChristineMa_9.jpg', description: 'Fresh bagels' },
      { id: 64, name: 'Pastry', category: 'bakery', price: 50, unit: '1 piece', image: 'https://krbakes.com/cdn/shop/articles/Top_10_Trending_Pastry_Cakes_You_Need_to_Try.webp?v=1739364407&width=1920', description: 'Fruit pastry' },
      
      // Electronics
      { id: 17, name: 'Smartphone', category: 'electronics', price: 15000, unit: '1 piece', image: 'https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=300&h=200&fit=crop', description: 'Latest smartphone' },
      { id: 18, name: 'Headphones', category: 'electronics', price: 2500, unit: '1 piece', image: 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=300&h=200&fit=crop', description: 'Wireless headphones' },
      { id: 19, name: 'Laptop', category: 'electronics', price: 45000, unit: '1 piece', image: 'https://images.unsplash.com/photo-1496181133206-80ce9b88a853?w=300&h=200&fit=crop', description: 'Gaming laptop' },
      { id: 20, name: 'Smart Watch', category: 'electronics', price: 8000, unit: '1 piece', image: 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=300&h=200&fit=crop', description: 'Fitness smart watch' },
      { id: 53, name: 'Tablet', category: 'electronics', price: 20000, unit: '1 piece', image: 'https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0?w=300&h=200&fit=crop', description: '10-inch tablet' },
      { id: 54, name: 'Power Bank', category: 'electronics', price: 1500, unit: '1 piece', image: 'https://i03.appmifile.com/333_item_in/08/07/2025/9047b35e12fa25cb45fc93a824a29e87.jpg', description: '10000mAh power bank' },
      { id: 55, name: 'Bluetooth Speaker', category: 'electronics', price: 3000, unit: '1 piece', image: 'https://images.unsplash.com/photo-1608043152269-423dbba4e7e1?w=300&h=200&fit=crop', description: 'Portable speaker' },
      { id: 56, name: 'Wireless Mouse', category: 'electronics', price: 800, unit: '1 piece', image: 'https://m.media-amazon.com/images/I/51vMo-pHZ5L.jpg', description: 'Ergonomic wireless mouse' },
      
      // Sports
      { id: 21, name: 'Football', category: 'sports', price: 800, unit: '1 piece', image: 'https://images.unsplash.com/photo-1486286701208-1d58e9338013?w=300&h=200&fit=crop', description: 'Professional football' },
      { id: 22, name: 'Cricket Bat', category: 'sports', price: 1200, unit: '1 piece', image: 'https://dkpcricketonline.com/cdn/shop/collections/image_419d887e-bcd5-4469-9925-776dc84db52b.heic?v=1754925807&width=2400', description: 'Professional cricket bat' },
      { id: 23, name: 'Cricket Ball', category: 'sports', price: 300, unit: '1 piece', image: 'https://nwscdn.com/media/catalog/product/cache/h400xw400/c/r/cricket-club-ball-family_1.jpg', description: 'Leather cricket ball' },
      { id: 24, name: 'Tennis Racket', category: 'sports', price: 1500, unit: '1 piece', image: 'https://us.yonex.com/cdn/shop/files/CLP_Tennis_Ezone_D.jpg?v=1740757953&width=1500', description: 'Professional tennis racket' },
      { id: 41, name: 'Basketball', category: 'sports', price: 900, unit: '1 piece', image: 'https://static.nbastore.in/resized/900X900/1180/wilson-nba-mens-drv-pro-basketball-brown-brown-68dc39e5a64de.jpg?format=webp', description: 'Professional basketball' },
      { id: 42, name: 'Badminton Racket', category: 'sports', price: 800, unit: '1 piece', image: 'https://cdn.firstcry.com/education/2022/07/25185734/Essay-On-My-Favourite-Game-Badminton-10-Lines-Short-and-Long-Essay-For-Kids.jpg', description: 'Lightweight badminton racket' },
      { id: 43, name: 'Table Tennis Paddle', category: 'sports', price: 400, unit: '1 piece', image: 'https://m.media-amazon.com/images/I/81OnewcSyTL.jpg', description: 'Professional table tennis paddle' },
      { id: 44, name: 'Volleyball', category: 'sports', price: 600, unit: '1 piece', image: 'https://m.media-amazon.com/images/I/61pFab9tNeL.jpg', description: 'Professional volleyball' },
      { id: 45, name: 'Yoga Mat', category: 'sports', price: 600, unit: '1 piece', image: 'https://images.unsplash.com/photo-1544367567-0f2fcb009e0b?w=300&h=200&fit=crop', description: 'Premium yoga mat' },
      { id: 46, name: 'Dumbbells', category: 'sports', price: 2000, unit: '1 pair', image: 'https://www.vinexshop.com/Product-Images/Large/2150-Dumbbells-Iron.jpg', description: 'Adjustable dumbbells' },
      { id: 47, name: 'Swimming Goggles', category: 'sports', price: 350, unit: '1 piece', image: 'https://rukminim2.flixcart.com/image/356/352/xif0q/goggle/r/i/s/-original-imahe3kahqp5zyfy.jpeg?q=90&crop=false', description: 'Anti-fog swimming goggles' },
      { id: 48, name: 'Boxing Gloves', category: 'sports', price: 1800, unit: '1 pair', image: 'https://m.media-amazon.com/images/I/81MThv+hgeS.jpg', description: 'Professional boxing gloves' },
      
      // Kids
      { id: 25, name: 'Teddy Bear', category: 'kids', price: 500, unit: '1 piece', image: 'https://tse1.mm.bing.net/th/id/OIP.IQUsCBaKM8Ox51lI1XH5BAHaFR?pid=Api&P=0&h=180', description: 'Soft teddy bear' },
      { id: 26, name: 'Building Blocks', category: 'kids', price: 800, unit: '1 set', image: 'https://baybee.co.in/cdn/shop/files/71Z7Rwn2BGL._SL1500.jpg?v=1735995897', description: 'Colorful building blocks' },
      { id: 27, name: 'Puzzle Game', category: 'kids', price: 300, unit: '1 piece', image: 'https://images.unsplash.com/photo-1601987177651-8edfe6c20009?fm=jpg&q=60&w=3000&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8cHV6emxlJTIwZ2FtZXxlbnwwfHwwfHx8MA%3D%3D', description: 'Educational puzzle' },
      { id: 28, name: 'Remote Car', category: 'kids', price: 1200, unit: '1 piece', image: 'https://cdn-gnhif.nitrocdn.com/TVcQuMoyCLAXtNubQjipHiuZSBgcXCHY/assets/images/optimized/rev-b921d37/www.daddydrones.in/image/cache/catalog/HOSPEED/HS16351/FRONT/image0-500x500.jpeg', description: 'RC racing car' },
      
      // Beauty
      { id: 29, name: 'Face Cream', category: 'beauty', price: 450, unit: '50ml', image: 'https://dr.rashel.in/cdn/shop/products/Vitamin_C_Face_Cream.jpg?v=1755964552', description: 'Anti-aging face cream' },
      { id: 30, name: 'Lipstick', category: 'beauty', price: 350, unit: '1 piece', image: 'https://images-static.nykaa.com/media/catalog/product/b/5/b560771773602685189_2.png?tr=w-500', description: 'Matte lipstick' },
      { id: 31, name: 'Shampoo', category: 'beauty', price: 250, unit: '200ml', image: 'https://barcodeprofessional.in/cdn/shop/files/01_7aaa4ca4-6c4e-44f7-816f-86b2f49489ef.jpg?v=1706353626', description: 'Hair care shampoo' },
      { id: 32, name: 'Perfume', category: 'beauty', price: 1500, unit: '100ml', image: 'https://images.pexels.com/photos/1961791/pexels-photo-1961791.jpeg?cs=srgb&dl=pexels-valeriya-1961791.jpg&fm=jpg', description: 'Luxury perfume' },
      { id: 49, name: 'Foundation', category: 'beauty', price: 800, unit: '30ml', image: 'https://media6.ppl-media.com//tr:h-235,w-235,c-at_max,dpr-2,q-40/static/img/product/344732/ny-bae-3-in-1-primer-foundation-serum-warm-cashew-03-30-ml-82_1_display_1754664234_9f6773f8.jpg', description: 'Liquid foundation' },
      { id: 50, name: 'Mascara', category: 'beauty', price: 400, unit: '1 piece', image: 'https://www.lakmeindia.com/cdn/shop/files/29112_H-8901030859073_800x.jpg?v=1742202692', description: 'Waterproof mascara' },
      { id: 51, name: 'Face Wash', category: 'beauty', price: 200, unit: '150ml', image: 'https://www.pinkroot.in/cdn/shop/files/orange-face-wash-for-tan-removalor-pimple-control-100ml-pink-root-1.png?v=1725009761', description: 'Gentle face wash' },
      { id: 52, name: 'Moisturizer', category: 'beauty', price: 350, unit: '100ml', image: 'https://plumgoodness.com/cdn/shop/files/nia-gel-moodshot-website.jpg?v=1760599846&width=1214', description: 'Daily moisturizer' }
    ];
    
    this.product = products.find(p => p.id === parseInt(id));
    if (this.product) {
      this.availableSizes = this.getSizesForProduct(this.product);
      if (this.availableSizes.length > 0) {
        this.selectedSize = this.availableSizes[0];
      }
    }
  }



  increaseQuantity() {
    this.quantity++;
  }

  decreaseQuantity() {
    if (this.quantity > 1) {
      this.quantity--;
    }
  }

  addToCart(product?: any) {
    const productToAdd = product || this.product;
    const quantityToAdd = product ? 1 : this.quantity;
    
    if (this.availableSizes.length > 0 && !this.selectedSize) {
      alert('Please select a size');
      return;
    }
    
    const cartItem = { ...productToAdd };
    if (this.selectedSize) {
      cartItem.size = this.selectedSize;
    }
    this.cartService.addToCart(cartItem, quantityToAdd);
  }

  toggleWishlist(): void {
    if (!this.authService.isAuthenticated()) {
      alert('Please login to add items to wishlist');
      return;
    }
    
    if (this.wishlistService.isInWishlist(this.product.id)) {
      this.wishlistService.removeFromWishlist(this.product.id).subscribe({
        next: () => this.wishlistService.loadWishlist()
      });
    } else {
      this.wishlistService.addToWishlist(this.product.id, this.product).subscribe({
        next: () => this.wishlistService.loadWishlist()
      });
    }
  }
}    