import { Injectable } from '@angular/core';
import { CupClass } from './cup-class';
import { CupInterface } from './cup-interface';


@Injectable()
export class CupService {
  public cupsList:CupInterface[] = [
    {
      "size":"S",
      "ml":250,
      "img":"assets/img/s-cup.png",
      "checked":false
    },
    {
      "size":"M",
      "ml":300,
      "img":"assets/img/m-cup.png",
      "checked":false
    },
    {
      "size":"L",
      "ml":350,
      "img":"assets/img/l-cup.png",
      "checked":false
    },
    {
      "size":"XL",
      "ml":450,
      "img":"assets/img/xl-cup.png",
      "checked":false
    }
  ];


  public cupProperties =  new CupClass();
  public definedCup =  new CupClass();

  public refreshCups():void{
    this.cupProperties = new CupClass();
    this.definedCup =  new CupClass();
    this.cupsList.forEach(item=>{
      item.checked = false;
    })
  }
  constructor() { }

}
