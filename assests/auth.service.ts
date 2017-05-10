// app/auth.service.ts

import { Injectable }      from '@angular/core';
import { tokenNotExpired } from 'angular2-jwt';
import { Router, NavigationStart } from '@angular/router';
import 'rxjs/add/operator/filter';
import { Http } from '@angular/http';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/toPromise';
import { IngridientInterface } from './ingridient-interface';
import { IngridientsService } from './ingridients.service';

// Avoid name not found warnings
declare var Auth0Lock: any;
declare var firebase: any;
@Injectable()
export class AuthService {
  // Configure Auth0
  lock = new Auth0Lock('7KoVexTTOhoChnqLdWoL4NvtaVFNnv0L', 'comanche.eu.auth0.com', {});
  userProfile: Object;
  public users = [];
  public usersExist = false;
  public isCurrentUserAdmin = false;
  constructor(public router: Router,private http: Http, public servedBaseList:IngridientsService) {
    this.userProfile = JSON.parse(localStorage.getItem('profile'));
    this
      .router
      .events
      .filter(event => event instanceof NavigationStart)
      .filter((event: NavigationStart) => (/access_token|id_token|error/).test(event.url))
      .subscribe(() => {
        this.lock.resumeAuth(window.location.hash, (error, authResult) => {
          if (error) return console.log(error);
          // Fetch profile information
          this.lock.getProfile(authResult.idToken, (error, profile) => {
            if (error) {
              // Handle error
              alert(error);
              return;
            }
            this.servedBaseList.fetchUsers().subscribe(
              (data) => {
                for(let u in data){
                  this.users.push(data[u])
                }
                this.users.forEach((u) => {
                  if(u.nickname === profile.nickname) {
                    this.isCurrentUserAdmin = u.isAdmin || false;
                    this.usersExist = true;
                  }
                });
                if(!this.usersExist) firebase.database().ref('/users').push(profile);
                window.localStorage.setItem("userName", profile.name);
                window.localStorage.setItem("nickName", profile.nickname);
                window.localStorage.setItem("isAdmin", this.isCurrentUserAdmin.toString());
                });
            this.userProfile = profile;
          });
          localStorage.setItem('id_token', authResult.idToken);
          this.router.navigate(['/']);
        });
      });
  }

  public login() {
    // Call the show method to display the widget.
    this.lock.show();
  }

  public authenticated() {
    // Check if there's an unexpired JWT
    // This searches for an item in localStorage with key == 'id_token'
    return tokenNotExpired();
  }

  public logout() {
    // Remove token from localStorage
    localStorage.removeItem('id_token');
    this.userProfile = undefined;
    window.localStorage.setItem("userName", "guest");
    window.localStorage.setItem("nickName", "");
    window.localStorage.setItem("isAdmin", "false");
  }
}
