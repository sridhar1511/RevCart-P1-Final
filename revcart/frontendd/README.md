# RevCart - E-Commerce Grocery Delivery Application

A modern Angular 18 frontend for the RevCart grocery delivery platform.

## Features

- **User Authentication** - Login, Register, Profile Management
- **Product Catalog** - Browse products by categories with search and filters
- **Shopping Cart** - Add/remove items with real-time updates
- **Checkout Process** - Address selection and payment methods
- **Order Management** - Track orders and view history
- **Responsive Design** - Mobile-friendly Bootstrap UI

## Technology Stack

- **Angular 18** - Frontend framework
- **Bootstrap 5** - UI styling
- **TypeScript** - Programming language
- **RxJS** - Reactive programming
- **Axios** - HTTP client (configured)

## Project Structure

```
src/
├── app/
│   ├── components/          # Reusable components
│   │   ├── header/
│   │   └── footer/
│   ├── pages/              # Page components
│   │   ├── home/
│   │   ├── products/
│   │   ├── cart/
│   │   ├── checkout/
│   │   ├── auth/
│   │   ├── profile/
│   │   └── orders/
│   ├── services/           # Business logic services
│   │   ├── auth.service.ts
│   │   ├── cart.service.ts
│   │   └── product.service.ts
│   ├── models/             # TypeScript interfaces
│   │   ├── user.model.ts
│   │   ├── product.model.ts
│   │   └── order.model.ts
│   └── guards/             # Route guards
├── assets/                 # Static assets
└── environments/           # Environment configurations
```

## Setup Instructions

### Prerequisites
- Node.js (v18 or higher)
- npm or yarn
- Angular CLI

### Installation

1. **Install dependencies:**
   ```bash
   npm install
   ```

2. **Install Angular CLI globally (if not installed):**
   ```bash
   npm install -g @angular/cli@18
   ```

3. **Start development server:**
   ```bash
   ng serve
   ```

4. **Open browser:**
   Navigate to `http://localhost:4200`

### Available Scripts

- `npm start` - Start development server
- `npm run build` - Build for production
- `npm test` - Run unit tests
- `npm run lint` - Run linting

## Key Components

### Pages
- **Home** - Landing page with categories and features
- **Products** - Product listing with filters and search
- **Product Detail** - Individual product information
- **Cart** - Shopping cart management
- **Checkout** - Order placement process
- **Auth** - Login and registration
- **Profile** - User profile and settings
- **Orders** - Order history and tracking

### Services
- **AuthService** - User authentication and session management
- **CartService** - Shopping cart state management
- **ProductService** - Product data operations

## Backend Integration

The frontend is configured to work with a Spring Boot backend API:
- Base URL: `http://localhost:8080/api`
- Authentication: JWT tokens
- Data format: JSON

## Deployment

1. **Build for production:**
   ```bash
   ng build --configuration production
   ```

2. **Deploy the `dist/` folder to your web server**

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Submit a pull request

## License

This project is licensed under the MIT License.