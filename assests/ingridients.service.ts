import { Injectable } from '@angular/core';
import { IngridientInterface } from './ingridient-interface'
import { Http } from '@angular/http';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/toPromise';

@Injectable()
export class IngridientsService {
  public ingridientsDataBase:IngridientInterface[] = [];
  public users = [];
  public filterList = [
    {category:"all", text:"All", state:true},
    {category:"syrops", text:"Syrops", state:false},
    {category:"chocolate", text:"Chocolate adds", state:false},
    {category:"milk", text:"Milky adds", state:false},
    {category:"alcohol", text:"Alcohol", state:false},
  ]

  public getIngridientsByType(type:string):IngridientInterface[]{
    return this.ingridientsDataBase.filter( (ingridient) => {
      return ingridient.type === type;
    });
  }

  constructor(private http: Http) { }

  public fetchData(){
    return this.http.get('https://coffeeangular.firebaseio.com/ingredients.json').map(
      (res) => res.json()
    );
  }
  public fetchCoffee(){
    return this.http.get('https://coffeeangular.firebaseio.com/definedCoffee.json').map(
      (res) => res.json()
    );
  }
  public fetchUsers(){
    return this.http.get('https://coffeeangular.firebaseio.com/users.json').map(
      (res) => res.json()
    );
  }
}
