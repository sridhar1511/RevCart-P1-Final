import { Component, OnInit, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-welcome-loader',
  templateUrl: './welcome-loader.component.html',
  styleUrls: ['./welcome-loader.component.css']
})
export class WelcomeLoaderComponent implements OnInit {
  @Output() loadingComplete = new EventEmitter<void>();

  ngOnInit() {
    setTimeout(() => {
      this.loadingComplete.emit();
    }, 3000);
  }
}