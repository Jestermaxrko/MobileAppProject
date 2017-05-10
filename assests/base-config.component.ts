import { Component, OnInit } from '@angular/core';
import { IngridientInterface } from '../ingridient-interface';
import { IngridientsService } from '../ingridients.service';
import { CupService } from '../cup.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-base-config',
  templateUrl: './base-config.component.html',
  styleUrls: ['./base-config.component.css']
})
export class BaseConfigComponent implements OnInit {
  public baseIngridientsList:IngridientInterface[];
  public showLoadder = true;
  constructor(private route:Router,private servedBaseList:IngridientsService, public cup:CupService) { }
  public chooseBase(obj:IngridientInterface):void{
    this.baseIngridientsList.forEach(item=>{
      item.checkState = false;
    })
    obj.checkState = true;
    this.cup.cupProperties.base = obj;
  }
  public countIngredientPrice(obj:IngridientInterface):number{
    return Math.floor(obj.price * this.cup.cupProperties.size.ml/250);
  }
  confirmBase(){
    this.cup.cupProperties.makeStepAvaliable('addsState');
    this.cup.cupProperties.makeStepActive('addsState');
    this.route.navigate(['config/adds'])
  }
  ngOnInit() {
    window.onbeforeunload = function (){
        return "";
    };
    window.onunload = function(event) {
      window.localStorage.setItem("redirect","true");
     }
     if(window.localStorage.getItem("redirect") === "true"){
       window.localStorage.setItem("redirect","false");
       this.route.navigate([''])
     }
    this.servedBaseList.fetchData().subscribe(
      (data) => {
        this.baseIngridientsList = data.filter(ingridient=>{
          return ingridient.type === 'base';
        });
        this.showLoadder = false;
        this.baseIngridientsList.forEach((it)=>{
          if(this.cup.cupProperties.base){
            if(it.name === this.cup.cupProperties.base.name){
              it.checkState = true;
              return;
            }
          }
        });
      });
    }
}
