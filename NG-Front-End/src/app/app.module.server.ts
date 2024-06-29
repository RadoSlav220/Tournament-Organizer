import { NgModule } from '@angular/core';
import { ServerModule } from '@angular/platform-server';

import { AppModule } from './app.module';
import { AppComponent } from './app.component';
import { CommonModule } from '@angular/common';

@NgModule({
  imports: [
    AppModule,
    ServerModule,
    CommonModule
  ],
  bootstrap: [AppComponent],
})
export class AppServerModule {}
