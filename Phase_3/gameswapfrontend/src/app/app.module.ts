import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { WelcomeComponent } from './welcome/welcome.component';
import { ListtemComponent } from './listtem/listtem.component';
import { MyitemComponent } from './myitem/myitem.component';
import { SearchitemsComponent } from './searchitems/searchitems.component';
import { SwaphistoryComponent } from './swaphistory/swaphistory.component';
import { UpdateinfoComponent } from './updateinfo/updateinfo.component';
import { HttpClientModule } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ProposeswapComponent } from './proposeswap/proposeswap.component';
import { MatRadioModule } from '@angular/material/radio'
import { MatInputModule } from '@angular/material/input'
import { MatTableModule } from '@angular/material/table'
import { MatButtonModule } from '@angular/material/button'
import { MatPaginatorModule } from '@angular/material/paginator'
import { MatSortModule } from '@angular/material/sort'
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { ViewitemComponent } from './viewitem/viewitem.component'
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatSelectModule } from '@angular/material/select';
import { AcknowledgeswapComponent } from './acknowledgeswap/acknowledgeswap.component';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { SwapdetailsComponent } from './swapdetails/swapdetails.component';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { ViewacceptrejectitemComponent } from './viewacceptrejectitem/viewacceptrejectitem.component';
import { RateswapComponent } from './rateswap/rateswap.component';
import { MyitemdetailsComponent } from './myitemdetails/myitemdetails.component';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    WelcomeComponent,
    ListtemComponent,
    MyitemComponent,
    SearchitemsComponent,
    SwaphistoryComponent,
    UpdateinfoComponent,
    ProposeswapComponent,
    ViewitemComponent,
    AcknowledgeswapComponent,
    SwapdetailsComponent,
    ViewacceptrejectitemComponent,
    RateswapComponent,
    MyitemdetailsComponent,    
  ],
  imports: [
    MatDialogModule,
    MatInputModule,
    MatTableModule,
    MatButtonModule,
    MatPaginatorModule,
    MatSortModule,
    MatProgressSpinnerModule,
    MatRadioModule,
    HttpClientModule,
    BrowserModule,
    FormsModule , 
    ReactiveFormsModule,
    AppRoutingModule,    
    BrowserAnimationsModule,        
    AppRoutingModule,
    FormsModule,
    MatFormFieldModule,
    BrowserAnimationsModule,
    MatInputModule,
    MatButtonModule,
    MatCheckboxModule,
    MatSelectModule,
    MatAutocompleteModule,
    MatSnackBarModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
