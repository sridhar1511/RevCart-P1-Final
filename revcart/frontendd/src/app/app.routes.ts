import { Routes } from '@angular/router';     
import { AuthGuard } from './guards/auth.guard';
import { AdminGuard } from './guards/admin.guard';
import { DeliveryAgentGuard } from './guards/delivery-agent.guard';

export const routes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { 
    path: 'test-api', 
    loadComponent: () => import('./test-api.component').then(m => m.TestApiComponent) 
  },
  { 
    path: 'home', 
    loadComponent: () => import('./pages/home/home.component').then(m => m.HomeComponent) 
  },
  { 
    path: 'products', 
    loadComponent: () => import('./pages/products/products.component').then(m => m.ProductsComponent) 
  },
  { 
    path: 'product/:id', 
    loadComponent: () => import('./pages/product-detail/product-detail.component').then(m => m.ProductDetailComponent) 
  },
  { 
    path: 'cart', 
    loadComponent: () => import('./pages/cart/cart.component').then(m => m.CartComponent),
    canActivate: [AuthGuard]
  },
  { 
    path: 'checkout', 
    loadComponent: () => import('./pages/checkout/checkout.component').then(m => m.CheckoutComponent),
    canActivate: [AuthGuard]
  },
  { 
    path: 'login', 
    loadComponent: () => import('./pages/auth/login/login.component').then(m => m.LoginComponent) 
  },
  { 
    path: 'register', 
    loadComponent: () => import('./pages/auth/register/register.component').then(m => m.RegisterComponent) 
  },
  { 
    path: 'forgot-password', 
    loadComponent: () => import('./pages/auth/forgot-password/forgot-password.component').then(m => m.ForgotPasswordComponent) 
  },
  { 
    path: 'reset-password', 
    loadComponent: () => import('./pages/auth/reset-password/reset-password.component').then(m => m.ResetPasswordComponent) 
  },
  { 
    path: 'profile', 
    loadComponent: () => import('./pages/profile/profile.component').then(m => m.ProfileComponent),
    canActivate: [AuthGuard]
  },
  { 
    path: 'orders', 
    loadComponent: () => import('./pages/orders/orders.component').then(m => m.OrdersComponent),
    canActivate: [AuthGuard]
  },
  { 
    path: 'wishlist', 
    loadComponent: () => import('./pages/wishlist/wishlist.component').then(m => m.WishlistComponent),
    canActivate: [AuthGuard]
  },
  { 
    path: 'coupons', 
    loadComponent: () => import('./pages/coupons/coupons.component').then(m => m.CouponsComponent)
  },
  { 
    path: 'admin', 
    loadComponent: () => import('./pages/admin/admin-dashboard.component').then(m => m.AdminDashboardComponent),
    canActivate: [AdminGuard]
  },
  { 
    path: 'admin/product/new', 
    loadComponent: () => import('./pages/admin/admin-product-form.component').then(m => m.AdminProductFormComponent),
    canActivate: [AdminGuard]
  },
  { 
    path: 'admin/product/:id', 
    loadComponent: () => import('./pages/admin/admin-product-form.component').then(m => m.AdminProductFormComponent),
    canActivate: [AdminGuard]
  },
  { 
    path: 'admin/analytics', 
    loadComponent: () => import('./pages/admin/analytics-dashboard.component').then(m => m.AnalyticsDashboardComponent),
    canActivate: [AdminGuard]
  },
  { 
    path: 'delivery-agent/register', 
    loadComponent: () => import('./pages/delivery-agent/delivery-agent-register.component').then(m => m.DeliveryAgentRegisterComponent) 
  },
  { 
    path: 'delivery-agent/login', 
    loadComponent: () => import('./pages/delivery-agent/delivery-agent-login.component').then(m => m.DeliveryAgentLoginComponent) 
  },
  { 
    path: 'delivery-agent/dashboard', 
    loadComponent: () => import('./pages/delivery-agent/delivery-agent-dashboard.component').then(m => m.DeliveryAgentDashboardComponent),
    canActivate: [DeliveryAgentGuard]
  },
  { 
    path: 'delivery-agent/profile', 
    loadComponent: () => import('./pages/delivery-agent/delivery-agent-profile.component').then(m => m.DeliveryAgentProfileComponent),
    canActivate: [DeliveryAgentGuard]
  },
  { 
    path: 'delivery-agent/tracking', 
    loadComponent: () => import('./pages/delivery-agent/delivery-tracking.component').then(m => m.DeliveryTrackingComponent),
    canActivate: [DeliveryAgentGuard]
  },
  { path: '**', redirectTo: '/home' }
];
