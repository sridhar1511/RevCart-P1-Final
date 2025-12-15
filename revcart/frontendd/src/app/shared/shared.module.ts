import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { WelcomeLoaderComponent } from '../components/welcome-loader/welcome-loader.component';

@NgModule({
  declarations: [
    WelcomeLoaderComponent
  ],
  imports: [
    CommonModule
  ],
  exports: [
    WelcomeLoaderComponent
  ]
})
export class SharedModule { }