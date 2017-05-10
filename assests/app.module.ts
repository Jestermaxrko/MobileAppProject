import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';

//Routes(navi)
import { routes } from './app.routes';


//Services
import { CupService } from './cup.service'
import { IngridientsService } from './ingridients.service';
import { AuthService } from './auth.service';

import { AppComponent } from './app.component';
import { PredefinedComponent } from './predefined/predefined.component';
import { SizeComponent } from './size/size.component';
import { ConfigComponent } from './config/config.component';
import { BaseConfigComponent } from './base-config/base-config.component';
import { AddsConfigComponent } from './adds-config/adds-config.component';
import { CupFillingComponent } from './cup-filling/cup-filling.component';

import {DragulaModule, DragulaService} from 'ng2-dragula/ng2-dragula';
import {TooltipModule} from "ngx-tooltip";
import { FinalComponent } from './final/final.component';

import { BootstrapModalModule } from 'ng2-bootstrap-modal';
import { SubmitModalComponent } from './submit-modal/submit-modal.component';
import { SuccessModalComponent } from './success-modal/success-modal.component';


@NgModule({
  declarations: [
    AppComponent,
    PredefinedComponent,
    SizeComponent,
    ConfigComponent,
    BaseConfigComponent,
    AddsConfigComponent,
    CupFillingComponent,
    FinalComponent,
    SubmitModalComponent,
    SuccessModalComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    routes,
    DragulaModule,
    TooltipModule,
    BootstrapModalModule
  ],
  entryComponents: [
    SubmitModalComponent,
    SuccessModalComponent
  ],
  providers: [
    CupService,
    IngridientsService,
    DragulaService,
    AuthService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
