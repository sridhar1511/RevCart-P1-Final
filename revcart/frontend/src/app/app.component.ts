import { Component, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HeaderComponent } from './components/header/header.component';
import { FooterComponent } from './components/footer/footer.component';
import { AuthService } from './services/auth.service';
import { CommonModule } from '@angular/common';
import { SharedModule } from './shared/shared.module';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, HeaderComponent, FooterComponent, CommonModule, SharedModule],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  title = 'RevCart';
  showWelcomeLoader = false;

  constructor(private authService: AuthService) {}

  ngOnInit() {
    this.authService.showWelcome$.subscribe(show => {
      this.showWelcomeLoader = show;
    });
  }

  onLoadingComplete() {
    this.authService.hideWelcome();
  }
}