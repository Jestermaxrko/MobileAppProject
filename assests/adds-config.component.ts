import { Component, OnInit } from '@angular/core';
import { IngridientInterface } from '../ingridient-interface';
import { IngridientsService } from '../ingridients.service';
import { CupService } from '../cup.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-adds-config',
  templateUrl: './adds-config.component.html',
  styleUrls: ['./adds-config.component.css']
})
export class AddsConfigComponent implements OnInit {
  public addsIngridientsList:IngridientInterface[];
  public showLoadder = true;
  constructor(private route:Router,public servedBaseList:IngridientsService, public cup:CupService) { }
  public addIngridient(obj:IngridientInterface):void{
    let ifExist:boolean = false;
    this.cup.cupProperties.adds.forEach(item=>{
      if(item.name === obj.name) ifExist=true;
    })
    if(!ifExist){
      obj.checkState = true;
      this.cup.cupProperties.adds.push(obj);
    }
  }
  public countIngredientPrice(obj:IngridientInterface):number{
    return Math.floor(obj.price * this.cup.cupProperties.size.ml/250);
  }
  public deleteComponent(obj:IngridientInterface):void{
    let removeIndex:number = 0;
    this.cup.cupProperties.adds.forEach((item, i)=>{
      if(obj.name === item.name){
        removeIndex = i;
        obj.checkState = false;
        return;
      }
    });
    this.addsIngridientsList.forEach((item, i)=>{
      if(obj.name === item.name){
        item.checkState = false;
        return;
      }
    })
    this.cup.cupProperties.adds.splice(removeIndex, 1);
  }

  public confirmAdds(){
    this.route.navigate(["config/fill-in"]);
    this.cup.cupProperties.makeStepActive('fillState');
    this.cup.cupProperties.makeStepAvaliable('fillState');
  }


  public filterBy(obj):void{
    //Disable active state for all filter buttons
    this.servedBaseList.filterList.forEach(item=>{
      item.state = false;
    })
    //Make active current button
    obj.state = true;

    this.addsIngridientsList = this.servedBaseList.getIngridientsByType('adds');
    if(obj.category==='all'){
      return;
    }
    this.addsIngridientsList = this.addsIngridientsList.filter(item=>{
      return item.addsType === obj.category;
    })
  }
  ngOnInit() {
    this.cup.cupProperties.adds.forEach((item)=>{
      item.checkState = false;
    });
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
        this.servedBaseList.ingridientsDataBase = data;
        this.addsIngridientsList = this.servedBaseList.ingridientsDataBase.filter(ingridient=>{
          return ingridient.type === 'adds';
        });
        this.showLoadder = false;
        this.addsIngridientsList.forEach((it)=>{
          this.cup.cupProperties.adds.forEach((item, i)=>{
            if(it.name === item.name){
              it.checkState = true;
              return;
            }
        });
      }
    );
  });
  }

}
