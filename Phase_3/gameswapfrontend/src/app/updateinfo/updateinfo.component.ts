import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { Observable } from 'rxjs';
import { map, startWith } from 'rxjs/operators';
import { Router } from '@angular/router';
import { GameswapService } from '../gameswap.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Subscription } from 'rxjs';

interface Type {
  value: string;
  viewValue: string;
}

@Component({
  selector: 'app-register',
  templateUrl: './updateinfo.component.html',
  styleUrls: ['./updateinfo.component.scss']
})
export class UpdateinfoComponent implements OnInit {
  constructor(private router:Router,
    private gameswapService: GameswapService,
    private _snackBar: MatSnackBar) { }

  userId: string;
  subscriptionUser: Subscription
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
    this.subscriptionUser = this.gameswapService.currentUser.subscribe(user => this.userId = user);
    // this.userId = 'user1@gatech.edu';

    Promise.all([this.gameswapService.getAcceptRejectSwap(), this.gameswapService.getUnratedSwaps(this.userId)])
    .then(responses => {
      let updateButton = document.querySelector("button#update_user_info_button");
      let updateErrorLabel = document.querySelector("mat-label#update_user_info_error_label");
      if (responses[0].length > 0 || responses[1].length > 0) {
        updateButton.classList.add('hidden');
        updateErrorLabel.classList.remove('hidden');
      } else {
        updateButton.classList.remove('hidden');
        updateErrorLabel.classList.add('hidden');
      }
    });

    this.gameswapService.getPostalCodes()
    .then((response: any[]) => {
      this.postalCodes = response;

      this.filteredPostalCodes = this.postalCodeFilterControl.valueChanges.pipe(
        startWith(''),
        map(value => this._filter(value))
      );

      this.gameswapService.getUserInfo(this.userId)
      .then(response => {
        this.email = response.email;
        this.nickname = response.nickName;
        this.postalCodeFilterControl.setValue(response.location.postalCode);
        this.firstName = response.firstName;
        this.lastName = response.lastName;
        if (response.phone) {
          this.phoneNumber = response.phone.number;
          this.type = response.phone.type;
          this.isShowPhoneNumberChecked = response.phone.share;
        }
      });
    });
  }

  onUpdate() {
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

    this.gameswapService.updateUser(
      this.email,
      this.password,
      this.firstName,
      this.lastName,
      this.nickname,
      this.postalCodeFilterControl.value,
      this.phoneNumber,
      this.type,
      this.isShowPhoneNumberChecked
    )
    .then(x => this._snackBar.open(x ? 'Update successful.' : `There is another registered user with the ${this.phoneNumber} phone number.`, "", {
      duration: 2000
    }))
    .catch(x => this._snackBar.open("Update failed.", "", {
      duration: 2000
    }));
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

  onBackToMain() {
    this.router.navigate(['/Welcome']);
  }

}
