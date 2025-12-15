import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class DeliveryAgentGuard implements CanActivate {
  constructor(private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    const deliveryAgent = localStorage.getItem('deliveryAgent');
    if (deliveryAgent) {
      return true;
    }
    this.router.navigate(['/delivery-agent/login'], { queryParams: { returnUrl: state.url } });
    return false;
  }
}