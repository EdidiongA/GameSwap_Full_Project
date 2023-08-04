import { Component, OnDestroy, OnInit, Inject } from '@angular/core';
import { Router } from '@angular/router';
import {SelectionModel} from "@angular/cdk/collections";
import { SwapItem } from '../models';
import { GameSwapItem } from '../models';
import { GameswapService } from '../gameswap.service';
import { Subscription } from 'rxjs';
import { MatTableDataSource } from '@angular/material/table';
import {MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material/dialog';
const queryParamName = "pippo";

export interface DialogData2 {
  id: string;  
  firstName: string;
  phoneNumber: string;
}

@Component({
  selector: 'app-acknowledgeswap',
  templateUrl: './acknowledgeswap.component.html',
  styleUrls: ['./acknowledgeswap.component.scss']
})
export class AcknowledgeswapComponent implements OnInit, OnDestroy {

  ELEMENT_DATA: SwapItem[] = [ 
  ];
  item: GameSwapItem
  subscriptionItem: Subscription;
  displayedColumns = ['Date', 'Desired Item', 'Proposer', 'Rating', 'Distance', 'Proposed Item', 'Ack'];
  dataSource =  new MatTableDataSource<SwapItem>(this.ELEMENT_DATA);
  selection: SelectionModel<SwapItem> = new SelectionModel<SwapItem>(false, []);
  
  constructor(private router:Router,
    private gameswapService: GameswapService,
    public dialog: MatDialog) { }

  ngOnInit(): void {
    this.subscriptionItem = this.gameswapService.currentAcceptRejectItemForViewDetails.subscribe(item => this.item = item)
   
    this.getAcceptRejectSwaps()
  }

  getAcceptRejectSwaps() {
    this.ELEMENT_DATA.splice(0,this.ELEMENT_DATA.length); 

    const promise = this.gameswapService.getAcceptRejectSwap()
    promise.then((data)=>{
      console.log(JSON.stringify(data));                       
    
      // This needs to be updated with desired_item_id and propose item id
      data.forEach(element => {
        this.ELEMENT_DATA.push(
          {
            swapId: 10,
            date: element.proposed_date,
            desiredItemId: element.counterparty_item_id,
            desiredItemTitle: element.desired_item,
            proposer: element.proposer,
            rating: element.rating,
            distance: element.distance,
            proposedItemId:element.proposed_item_id,
            proposedItemTittle:element.proposed_item
          })   
      });
     
       
      this.dataSource = new MatTableDataSource<SwapItem>(this.ELEMENT_DATA);;
    }).catch((error)=>{
      console.log("Promise rejected with " + JSON.stringify(error));
    });  
  }

  ngOnDestroy() {
    this.subscriptionItem.unsubscribe()
  }

  onBackToMain() {
    this.router.navigate(['/Welcome']);
  }  
 
  openDialog(email, firstName, phoneNumber): void {
    const dialogRef = this.dialog.open(AcceptDialog, {
      width: '250px',
      height: '400px',
      data: {
        id: email,
        firstName: firstName,
        phoneNumber: phoneNumber,
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');        
    });
  }

  onAccept(row: SwapItem) {
    console.log('Accept - ' + row)   
    
    const promise = this.gameswapService.postAcceptSwap(row.desiredItemId, row.proposedItemId)
    promise.then((data)=>{
      console.log(JSON.stringify(data));   
      
      this.getAcceptRejectSwaps()

      const promise = this.gameswapService.getItemDetails(row.proposedItemId)
      promise.then((data)=>{
        console.log(JSON.stringify(data));   
               
        var id = data.itemOwner.user.email
        //var firstName = 'need to wait for API fix'
        var firstName = data.itemOwner.user.firstName
        var phoneNumber = 'NA'
        if(data.itemOwner.user.phone.share)
        {
          phoneNumber = data.itemOwner.user.phone.number + '(' + data.itemOwner.user.phone.type + ')'
        }

        this.openDialog(id, firstName, phoneNumber)
  
      }).catch((error)=>{
        console.log("Promise rejected with " + JSON.stringify(error));
      }); 


    }).catch((error)=>{
      console.log("Promise rejected with " + JSON.stringify(error));
    });  
  }
  onReject(row: SwapItem) {
    console.log('Reject - ' + row)

    const promise = this.gameswapService.postRejecttSwap(row.desiredItemId, row.proposedItemId)
    promise.then((data)=>{
      console.log(JSON.stringify(data));   
      
      this.getAcceptRejectSwaps()
    }).catch((error)=>{
      console.log("Promise rejected with " + JSON.stringify(error));
    });  
  }

  onDesiredItemDetails(row) {

    this.item = {
      itemId: row.desiredItemId,
      type: 'video game',
      title: row.desiredItemTitle,
      condition: 'good',
      description: '',
      distance: row.distance,
      details: 'details',
      selected: false
    }
    console.log(this.item)    
    this.gameswapService.updateSelectedAcceptRejectItemForViewDetails(this.item);
    this.router.navigate(['/ViewAcceptRejectItemDetails']);
  }
    onProposedItemDetails(row) {
  
      this.item = {
        itemId: row.proposedItemId,
        type: 'video game',
        title: row.proposedItemTittle,
        condition: 'good',
        description: '',
        distance: row.distance,
        details: 'details',
        selected: false
      }
  
    console.log(this.item)    
    this.gameswapService.updateSelectedAcceptRejectItemForViewDetails(this.item);
    this.router.navigate(['/ViewAcceptRejectItemDetails']);
  }
}

@Component({
  selector: 'popup2',
  templateUrl: './popup2.html',
})
export class AcceptDialog {

  constructor(
    public dialogRef: MatDialogRef<AcceptDialog>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData2) {}

  onOK(): void {
    this.dialogRef.close();
  }

}