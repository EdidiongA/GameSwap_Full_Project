import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { Observable } from 'rxjs';
import { map, startWith } from 'rxjs/operators';
import { Router } from '@angular/router';
import { GameswapService } from '../gameswap.service';
import { MatSnackBar } from '@angular/material/snack-bar';

interface Type {
  value: string;
  viewValue: string;
}

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {
  constructor(private router:Router,
    private gameswapService: GameswapService,
    private _snackBar: MatSnackBar) { }

  email
  nickname
  password
  city
  firstName
  state
  lastName
  phoneNumber
  type
  isShowPhoneNumberChecked

  postalCodeFilterControl = new FormControl();
  postalCodes: any[] = [];
  filteredPostalCodes: Observable<string[]>;

  types: Type[] = [
    {value: 'Home', viewValue: 'Home'},
    {value: 'Work', viewValue: 'Work'},
    {value: 'Mobile', viewValue: 'Mobile'}
  ];

  ngOnInit(): void {
    this.gameswapService.getPostalCodes().then((response: any[]) => {
      this.postalCodes = response;

      this.filteredPostalCodes = this.postalCodeFilterControl.valueChanges.pipe(
        startWith(''),
        map(value => this._filter(value))
      );
    });
  }

  onRegister() {
    // console.log(this.email);
    // console.log(this.nickname);
    // console.log(this.password);
    // console.log(this.city);
    // console.log(this.firstName);
    // console.log(this.state);
    // console.log(this.lastName);
    // console.log(this.postalCodeFilterControl.value);
    // console.log(this.phoneNumber);
    // console.log(this.type);
    // console.log(this.isShowPhoneNumberChecked);

    if (!this.email.includes('@')) {
      this._snackBar.open("Registration failed due to invalid email", "", {
        duration: 2000
      });
      return;
    }

    this.gameswapService.addUser(
      this.email,
      this.password,
      this.firstName,
      this.lastName,
      this.nickname,
      this.postalCodeFilterControl.value,
      this.phoneNumber,
      this.type,
      this.isShowPhoneNumberChecked
    ).then(x => {
      this.gameswapService.updateUserId(this.email);
      this.router.navigate(['/Welcome'])
    }).catch(
      x => this._snackBar.open("Registration failed", "", {
        duration: 2000
      })
    );
  }

  private _filter(value: String): any[] {
    console.log(value);
    let filtered = this.postalCodes.filter(postalCode => postalCode.postalCode.startsWith(value));
    if (filtered.length > 500) {
      filtered.splice(500);
      filtered.push({ postalCode: '...' });
    }

    if (filtered.length == 1) {
      this.city = filtered[0].city;
      this.state = filtered[0].state;
    } else {
      this.city = undefined;
      this.state = undefined;
    }

    return filtered;
  }

}
