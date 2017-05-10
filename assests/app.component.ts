import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from './auth.service';
import { IngridientInterface } from './ingridient-interface';
import { IngridientsService } from './ingridients.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  constructor(private route:Router, private auth: AuthService,public servedBaseList:IngridientsService){
   }
   public getcurrentUser(){
     return {name:window.localStorage.getItem("userName") || "guest", isAdmin:window.localStorage.getItem("isAdmin") === "true" || false};
   }
  ngOnInit(){
  }
}
