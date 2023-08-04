import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { ListtemComponent } from './listtem/listtem.component';
import { MyitemComponent } from './myitem/myitem.component';
import { RegisterComponent } from './register/register.component';
import { SearchitemsComponent } from './searchitems/searchitems.component';
import { SwaphistoryComponent } from './swaphistory/swaphistory.component';
import { UpdateinfoComponent } from './updateinfo/updateinfo.component';
import { WelcomeComponent } from './welcome/welcome.component';
import { ProposeswapComponent } from './proposeswap/proposeswap.component';
import { ViewitemComponent } from './viewitem/viewitem.component';
import { AcknowledgeswapComponent } from './acknowledgeswap/acknowledgeswap.component';
import { SwapdetailsComponent } from './swapdetails/swapdetails.component';
import { ViewacceptrejectitemComponent } from './viewacceptrejectitem/viewacceptrejectitem.component';
import { RateswapComponent } from './rateswap/rateswap.component';
import { MyitemdetailsComponent } from './myitemdetails/myitemdetails.component';

const routes: Routes = [  
  { path: '', redirectTo: 'Login', pathMatch: 'full' },
  {
      path: 'Login',
      component: LoginComponent      
  },
  {
      path: 'ListItem',
      component: ListtemComponent,        
  },
  {
      path: 'MyItem',
      component: MyitemComponent,      
  },
  {
      path: 'Register',
      component: RegisterComponent,        
  },
  {
      path: 'SearchItem',
      component: SearchitemsComponent,        
  },
  {
      path: 'SwapHistory',
      component: SwaphistoryComponent,        
  },
  {
      path: 'UpdateInfo',
      component: UpdateinfoComponent,        
  },
  {
      path: 'Welcome',
      component: WelcomeComponent,        
  },
  {
      path: 'ProposeSwap',
      component: ProposeswapComponent,        
  },
  {
      path: 'ViewItem',
      component: ViewitemComponent,        
  },
  {
      path: 'AcknowledgeSwap',
      component: AcknowledgeswapComponent,        
  },
  {
      path: 'SwapDetails',
      component: SwapdetailsComponent,
  },
  {
      path: 'ViewAcceptRejectItemDetails',
      component: ViewacceptrejectitemComponent,
  },
  {
      path: 'RateSwap',
      component: RateswapComponent,
  },
  {
      path: 'ViewMyItemDetails',
      component: MyitemdetailsComponent,
  },
    
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
