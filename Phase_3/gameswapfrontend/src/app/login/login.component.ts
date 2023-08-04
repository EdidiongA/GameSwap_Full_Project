import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms'; 
import { ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { GameswapService } from '../gameswap.service';
import { MatSnackBar } from '@angular/material/snack-bar';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  constructor(private router:Router,
    private gameswapService: GameswapService,
    private _snackBar: MatSnackBar,) { }

  loginId
  password
  loginError
  ngOnInit(): void {
  }

  onLogin() {
    //this.gameswapService.addUser()
    const promise = this.gameswapService.getAuthentication(this.loginId, this.password)
    promise.then((data)=>{
      console.log(JSON.stringify(data));
                
      this.loginId = data
      this.gameswapService.updateUserId(this.loginId)
      this.router.navigate(['/Welcome']);
     
    }).catch((error)=>{
      console.log("Promise rejected with " + JSON.stringify(error));
      this._snackBar.open("Login failed", "", {
        duration: 2000
      })
    });
  }

  onRegister() {
    this.router.navigate(['/Register']);
  }
}
