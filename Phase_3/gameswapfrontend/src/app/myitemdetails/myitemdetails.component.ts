import { Component, OnInit, OnDestroy } from '@angular/core';
import { GameswapService } from '../gameswap.service';
import { GameSwapItem } from '../models';
import { Subscription } from 'rxjs';
import { Router } from '@angular/router';

@Component({
  selector: 'app-myitemdetails',
  templateUrl: './myitemdetails.component.html',
  styleUrls: ['./myitemdetails.component.scss']
})
export class MyitemdetailsComponent implements OnInit, OnDestroy {

  count
  media
  title
  type
  platform
  condition  
  offerBy
  location
  rating
  showMedia = false
  showPlatform = false
  showCount = false
  showDescription = true
  item: GameSwapItem
  subscriptionItem: Subscription;
  counterpartyPostal
  description
  userId: string;
  subscriptionUser: Subscription;
  secondColumnClass = "";

  constructor(private gameswapService: GameswapService,private router:Router) { }

  ngOnInit(): void {
    this.subscriptionUser = this.gameswapService.currentUser.subscribe(user => this.userId = user);
    this.subscriptionItem = this.gameswapService.currentMyItemForViewDetails.subscribe(item => this.item = item)
    const promise = this.gameswapService.getItemDetails(this.item.itemId)
    promise.then((data)=>{
      this.secondColumnClass = this.userId == data.itemOwner.user.email ? "hidden" : "";
      console.log(JSON.stringify(data));  
      this.title = data.name
      this.condition = data.condition
      this.type = data.gameType
      this.media = "NA"
      this.platform = "NA"
      this.count = "NA"
      this.description = data.fullDescription
      if(this.description == null || this.description == '')
      {
        this.showDescription = false
      }

      if(this.type == "Computer Game")
      {
        this.platform = data.gameTypeMetadata.computerGamePlatform
        this.showPlatform = true
      }
      else if(this.type == "Video Game")
      {
        this.platform = data.gameTypeMetadata.videoGamePlatform
        this.media = data.gameTypeMetadata.videoGameMedia
        this.showPlatform = true
        this.showMedia = true
      }
      else if(this.type == "Jigsaw Puzzle")
      {
        this.count = data.gameTypeMetadata.jigsawPuzzlePieceCount        
        this.showCount = true
      }
      this.offerBy = data.itemOwner.user.nickName;
      this.location = data.itemOwner.user.location.city + "," + data.itemOwner.user.location.state + " " + data.itemOwner.user.location.postalCode;
      this.rating = 'None'
      if(data.itemOwner.rating != null)
      {
        this.rating = data.itemOwner.rating;     
      }

    }).catch((error)=>{
      console.log("Promise rejected with " + JSON.stringify(error));
    });  
  }

  ngOnDestroy() {
    this.subscriptionItem.unsubscribe()
  }

  onBack() {
    this.router.navigate(['/MyItem']);
  }
}
