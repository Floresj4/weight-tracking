import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

import { TableModule } from 'primeng/table';
import { CardModule } from 'primeng/card';
import { DropdownModule } from 'primeng/dropdown';

import { TrendDisplayComponent } from './components/trend-display/trend-display.component';
import { TableDisplayComponent } from './components/table-display/table-display.component';


@NgModule({
  declarations: [
    AppComponent,
    TrendDisplayComponent,
    TableDisplayComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    AppRoutingModule,
    HttpClientModule,
    CardModule,
    DropdownModule,
    TableModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
