import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ProductService } from '../../services/product.service';

@Component({
  selector: 'app-admin-product-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-product-form.component.html',
  styleUrls: ['./admin-product-form.component.scss']
})
export class AdminProductFormComponent implements OnInit {
  product = {
    name: '',
    description: '',
    price: 0,
    category: '',
    stockQuantity: 0,
    image: '',
    unit: ''
  };
  
  categories = ['Vegetables', 'Fruits', 'Dairy', 'Bakery', 'Beverages', 'Snacks', 'Spices', 'Oils'];
  units = ['kg', 'liter', 'piece', 'dozen', 'pack', 'box'];
  isLoading = false;
  isEditMode = false;
  productId: number | null = null;
  message = '';
  messageType = '';

  constructor(
    private productService: ProductService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit() {
    this.route.params.subscribe(params => {
      if (params['id']) {
        this.isEditMode = true;
        this.productId = params['id'];
        this.loadProduct();
      }
    });
  }

  loadProduct() {
    if (!this.productId) return;
    this.productService.getProductById(this.productId).subscribe({
      next: (product) => {
        this.product = product;
      },
      error: (error) => {
        this.showMessage('Error loading product', 'danger');
      }
    });
  }

  saveProduct() {
    if (!this.validateForm()) return;
    
    this.isLoading = true;
    const request = {
      name: this.product.name,
      description: this.product.description,
      price: this.product.price,
      category: this.product.category,
      stockQuantity: this.product.stockQuantity,
      image: this.product.image,
      unit: this.product.unit
    };

    if (this.isEditMode && this.productId) {
      this.productService.updateProduct(this.productId, request).subscribe({
        next: (response) => {
          this.showMessage('Product updated successfully', 'success');
          setTimeout(() => this.router.navigate(['/admin']), 1500);
        },
        error: (error) => {
          this.showMessage('Error updating product', 'danger');
          this.isLoading = false;
        }
      });
    } else {
      this.productService.createProduct(request).subscribe({
        next: (response) => {
          this.showMessage('Product created successfully', 'success');
          setTimeout(() => this.router.navigate(['/admin']), 1500);
        },
        error: (error) => {
          this.showMessage('Error creating product', 'danger');
          this.isLoading = false;
        }
      });
    }
  }

  validateForm(): boolean {
    if (!this.product.name || !this.product.description || !this.product.category) {
      this.showMessage('Please fill all required fields', 'warning');
      return false;
    }
    if (this.product.price <= 0 || this.product.stockQuantity <= 0) {
      this.showMessage('Price and stock must be greater than 0', 'warning');
      return false;
    }
    return true;
  }

  showMessage(msg: string, type: string) {
    this.message = msg;
    this.messageType = type;
    setTimeout(() => this.message = '', 3000);
  }

  cancel() {
    this.router.navigate(['/admin']);
  }
}
