import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-product-card',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './product-card.component.html',
  styleUrls: ['./product-card.component.css']
})
export class ProductCardComponent {
  @Input() product: any;
  @Output() addToCart = new EventEmitter<any>();

  onAddToCart() {
    this.addToCart.emit(this.product);
  }

  onImageError(event: any) {
    event.target.src = 'https://via.placeholder.com/300x200?text=No+Image';
  }
}