import { ModuleWithProviders } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";


import { AppComponent } from './app.component';
import { PredefinedComponent } from './predefined/predefined.component';
import { SizeComponent } from './size/size.component';
import { ConfigComponent } from './config/config.component';
import { BaseConfigComponent } from './base-config/base-config.component';
import { AddsConfigComponent } from './adds-config/adds-config.component';
import { CupFillingComponent } from './cup-filling/cup-filling.component';
import { FinalComponent } from './final/final.component';


export const router: Routes = [
  {path:'', component: PredefinedComponent},
  {path:'config', component: ConfigComponent, children:[
    {path:'', redirectTo: 'size', pathMatch:'full'},
    {path:'size', component: SizeComponent},
    {path:'base', component: BaseConfigComponent },
    {path:'adds', component: AddsConfigComponent },
    {path:'fill-in', component: CupFillingComponent },
  ]},
  {path:'final', component: FinalComponent },
]

export const routes: ModuleWithProviders = RouterModule.forRoot(router);
