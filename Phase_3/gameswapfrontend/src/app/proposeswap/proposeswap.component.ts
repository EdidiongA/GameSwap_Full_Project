import { Component, OnInit, OnDestroy, Inject } from '@angular/core';
import {SelectionModel} from "@angular/cdk/collections";
import { GameSwapItem } from '../models';
import { Router } from '@angular/router';
import { GameswapService } from '../gameswap.service';
import { Subscription } from 'rxjs';
import {MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material/dialog';

const queryParamName = "pippo";
export interface DialogData3{
  id: string;  
}


@Component({
  selector: 'app-proposeswap',
  templateUrl: './proposeswap.component.html',
  styleUrls: ['./proposeswap.component.scss']
})
export class ProposeswapComponent implements OnInit {

  
  userId: String
  subscriptionUser: Subscription;
  
  selectedItem: GameSwapItem
  subscriptionSelectedItem: Subscription;
  distanceAway

  swapItem
  constructor(private router:Router,
    private gameswapService: GameswapService,
    public dialog: MatDialog) { }

  showDistanceAway = false
  displayedColumns = ['Item #', 'Game Type', 'Title', 'Condition', 'selection'];
  dataSource
  selection: SelectionModel<GameSwapItem> = new SelectionModel<GameSwapItem>(false, []);
  ngOnInit():void{

    ELEMENT_DATA.splice(0,ELEMENT_DATA.length);
    this.subscriptionUser = this.gameswapService.currentUser.subscribe(user => this.userId = user)
    this.subscriptionSelectedItem = this.gameswapService.currentItem.subscribe(item => this.selectedItem = item)

    this.distanceAway = this.selectedItem.distance;

    if(Number(this.distanceAway) > 100)
    {
      this.showDistanceAway = true
    }

    const promise = this.gameswapService.getItemForSwap(this.selectedItem.itemId)
    promise.then((data)=>{
      console.log(JSON.stringify(data));
      data.forEach(element => {
        ELEMENT_DATA.push(
          {
            itemId: element.id,
            type: 'video game',
            title: element.name,
            condition: element.condition,
            description: element.description,
            distance: 0,
            details: '',
            selected: false
          })
      });                  
      this.dataSource = ELEMENT_DATA;
      //this.name = data.firstName + ' ' + data.lastName
      //this.myrating = data.userStats.rating
      //this.unacceptedSwaps = data.userStats.unacceptedSwapCount
      //this.unratedSwaps = data.userStats.unratedSwapCount
    
    }).catch((error)=>{
      console.log("Promise rejected with " + JSON.stringify(error));
    });  
           
    
  }

  ngOnDestroy(): void {
    this.subscriptionUser.unsubscribe()
  }

  selectRow($event: any, row:GameSwapItem){
    console.info("clicked", $event);
    console.info(row);
    $event.preventDefault();
        if (!row.selected) {
            this.dataSource.forEach((row) => row.selected = false);
            row.selected = true;
            this.selection.select(row);
            if (location.href.indexOf(queryParamName) >= 0) {
                location.href = location.href.replace(queryParamName, "");
            }
        }
  }

  openDialog(): void {
    const dialogRef = this.dialog.open(ProposeDialog, {
      width: '250px',
      height: '400px',     
      data: {
        id: '',
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');        
    });
  }

  onConfirm() {
    this.dataSource.forEach(element => {
      if(element.selected)
      {
        const promise = this.gameswapService.getItemDetails(this.selectedItem.itemId)
        promise.then((data)=>{
          console.log(JSON.stringify(data));                       
          var counterPartyID = data.itemOwner.user.email;

          
          const innerPromise = this.gameswapService.postConfirmSwap(counterPartyID, this.selectedItem.itemId, element.itemId)
          innerPromise.then((data)=>{
            this.openDialog()
            this.router.navigate(['/Welcome']);
          }).catch((error)=>{
            console.log("Promise rejected with " + JSON.stringify(error));
          });  
        }).catch((error)=>{
          console.log("Promise rejected with " + JSON.stringify(error));
        });  
        
      }
    });
  }

  onBack() {
    this.router.navigate(['/SearchItem']);
  }
}


@Component({
  selector: 'popup3',
  templateUrl: './popup3.html',
})
export class ProposeDialog {

  constructor(
    public dialogRef: MatDialogRef<ProposeDialog>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData3) {}

  onOK(): void {
    this.dialogRef.close();
  }

}
// export interface Element {
//   itemId: number;
//   type: string;
//   title: string;
//   condition: string;
//   selected: boolean;
// }

const ELEMENT_DATA: GameSwapItem[] = [ 
];