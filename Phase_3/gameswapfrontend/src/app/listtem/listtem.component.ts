import { Component, OnInit, Inject } from '@angular/core';
import { Router } from '@angular/router';
import { FormControl, FormGroup, FormsModule, NgForm } from '@angular/forms'; 
import { GameswapService } from '../gameswap.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { LoginComponent } from '../login/login.component';
import {MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material/dialog';


interface Type {
  value: string;
  viewValue: string;
} 

export interface DialogData {
  id: string;  
}

@Component({
  selector: 'app-listtem',
  templateUrl: './listtem.component.html',
  styleUrls: ['./listtem.component.scss']
})

export class ListtemComponent implements OnInit {

  constructor(private router:Router, 
    private gameswapService: GameswapService,
    private infoSnackBar: MatSnackBar,
    public dialog: MatDialog) { 
      const promise = this.gameswapService.getvideoGamePlatforms()
      promise.then((data)=>{
        console.log(JSON.stringify(data));                 
        data.forEach(element => {
          this.platforms.push(element)
        });
        
      }).catch((error)=>{
        console.log("Promise rejected with " + JSON.stringify(error));
        this.error = this.gameType + " has been not added."
      });  

      const promise2 = this.gameswapService.getcurrentUserInfo()
      promise2.then((data2)=>{
       
        if(data2.userStats.unratedSwapCount > 2 || data2.userStats.unacceptedSwapCount > 5)
        {
          this.isDisabled = true
        }
        
      }).catch((error)=>{
        console.log("Promise rejected with " + JSON.stringify(error));
        this.error = this.gameType + " has been not added."
      });  

      
    }

    openDialog(): void {
      const dialogRef = this.dialog.open(ListItemDialog, {
        width: '250px',
        data: {id: this.id}
      });
  
      dialogRef.afterClosed().subscribe(result => {
        console.log('The dialog was closed');        
      });
    }
  error
  gameType
  platform
  platforms = []
  platformOS
  media
  title
  pieceCount
  condition
  description
  id

  isDisabled = false
  ngOnInit(): void {
   
  }

  onGameTypeSelect() {
    //called wnen a a game type is selected. 
        
  } 
  chosenPlatform: string;

  listItem() {
    
    switch(this.gameType){
      case "Video Game":
        this.chosenPlatform = this.platform;
        break;
      case "Computer Game":
        this.chosenPlatform = this.platformOS;
        break;
      case "Jigsaw Puzzle":
        this.media= undefined;
        this.platform, this.platformOS  = undefined;
        break;
      default:   
        this.media= undefined;
        this.platform, this.platformOS  = undefined;
        this.pieceCount = undefined;
    }
   
    this.printToLog();  //show data in log
    this.saveListItem();  //save item to repository
  }

  printToLog(){
    
  }
  
  saveListItem(){
    //saves new item to repository
    /*
    this.gameswapService.addItem(
      this.title,
      this.gameType,
      this.platform, 
      this.media,
      this.pieceCount, 
      this.condition,
      this. description
    ).then(
       w =>this.infoSnackBar.open((<string>this.gameType) + " Has been added.", "", {
          duration: 3000
       })
    ).catch(
      w => this.infoSnackBar.open((<string>this.gameType) + " NOT added", "", {
        duration: 3000
      })
    );
    */

    if(this.condition == null)
    {
      this.error = "Please enter condition"
      return
    }  
    else if(this.gameType == null)
    {
      this.error = "Please select game type"
      return
    }
    else if(this.gameType == "Jigsaw Puzzle" && this.pieceCount == null)
    {
      this.error = "Please enter pieces"
      return
    }
    else if(this.gameType == "Computer Game" && this.platformOS == null)
    {
      this.error = "Please enter platform"
      return
    }
    else if(this.gameType == "Video Game" && this.platform == null)
    {
      this.error = "Please enter platform"
      return
    }
    else if(this.gameType == "Video Game" && this.media == null)
    {
      this.error = "Please select media"
      return
    }
    else if(this.title == null)
    {
      this.error = "Please enter title"
      return
    }

    var jigsawPuzzlePieceCount =0;
    if(this.pieceCount != null)
    {
      jigsawPuzzlePieceCount = Number(this.pieceCount) 
    }

    var videoGameMedia = "Optical disc"
    var computerGamePlatform = "Linux"
    var videoGamePlatform = "Nintendo"
    if(this.gameType == "Video Game")
    {
      videoGamePlatform = this.platform
      videoGameMedia = this.media
    }
    if(this.gameType == "Computer Game")
    {
      computerGamePlatform = this.platformOS
    }
   
    const promise = this.gameswapService.postListItem(this.condition,  this.description, this.gameType, computerGamePlatform, jigsawPuzzlePieceCount, videoGameMedia, videoGamePlatform, this.title)
    promise.then((data)=>{
      console.log(JSON.stringify(data));                 
      //this.error = this.gameType + " has been added."
      this.id = data
      this.openDialog()
      
    }).catch((error)=>{
      console.log("Promise rejected with " + JSON.stringify(error));
      this.error = this.gameType + " has been not added."
    });  
    // this.gameType.reset();
    this.condition = null
    this.description = null
    this.gameType = null
    this.platform = null
    this.pieceCount = null
    this.media = null
    this.platform = null
    this.title = null
  }
  

  onBackToMain() {
    this.router.navigate(['/Welcome']);
  }
}

@Component({
  selector: 'popup',
  templateUrl: './popup.html',
})
export class ListItemDialog {

  constructor(
    public dialogRef: MatDialogRef<ListItemDialog>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData) {}

  onOK(): void {
    this.dialogRef.close();
  }

}