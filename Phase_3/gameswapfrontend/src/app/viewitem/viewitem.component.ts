import { Component, OnInit, OnDestroy } from '@angular/core';
import { GameswapService } from '../gameswap.service';
import { GameSwapItem } from '../models';
import { Subscription } from 'rxjs';
import { Router } from '@angular/router';

@Component({
  selector: 'app-viewitem',
  templateUrl: './viewitem.component.html',
  styleUrls: ['./viewitem.component.scss']
})
export class ViewitemComponent implements OnInit, OnDestroy {

  constructor(private gameswapService: GameswapService,private router:Router) { }
  offerBy
  location
  rating  
  description
  platform
  item: GameSwapItem
  subscriptionItem: Subscription;
  media
  showGreen = false
  showRed = false
  showYellow = false
  showOrange = false
  showPlatform = false
  showMedia = false
  counterpartyPostal
  showSwap = true
  showDescription = true
  showCount = false
  title 
  userId: string;
  subscriptionUser: Subscription;
  secondColumnClass = "";
  count

  ngOnInit(): void {

  this.subscriptionUser = this.gameswapService.currentUser.subscribe(user => this.userId = user);
  this.subscriptionItem = this.gameswapService.currentItem.subscribe(item => this.item = item)

  const promise = this.gameswapService.getItemDetails(this.item.itemId)
  promise.then((data)=>{
    this.secondColumnClass = this.userId == data.itemOwner.user.email ? "hidden" : "";
    console.log(JSON.stringify(data));                       
    this.offerBy = data.itemOwner.user.nickName;
    this.location = data.itemOwner.user.location.city + "," + data.itemOwner.user.location.state + " " + data.itemOwner.user.location.postalCode;
    this.rating = 'None'
    if(data.itemOwner.rating != null)
    {
      this.rating = data.itemOwner.rating;
    }
    this.description = data.description;
    this.item.fullDescription = data.fullDescription;
    if(this.description == null || this.description == '')
    {
      this.showDescription = false
    }
    if(this.item.type == "Computer Game")
    {
      this.platform = data.gameTypeMetadata.computerGamePlatform
      this.showPlatform = true
    }
    else if(this.item.type == "Video Game")
    {
      this.showPlatform = true
      this.showMedia = true
      this.media = data.gameTypeMetadata.videoGameMedia
      this.platform = data.gameTypeMetadata.videoGamePlatform
    }   
    else if(this.item.type == "Jigsaw Puzzle")
    {
      this.showCount = true
      this.count = data.gameTypeMetadata.jigsawPuzzlePieceCount
    } 

    this.title = data.name
    this.counterpartyPostal = data.itemOwner.user.location.postalCode;
    const promise2 = this.gameswapService.getcurrentUserInfo()
    promise2.then((data2)=>{
      if(data2.location.postalCode != data.itemOwner.user.location.postalCode)
      {
        if(this.item.distance > 0 && this.item.distance < 25)
        {
          this.showGreen = true
        }
        else if(this.item.distance >= 25 && this.item.distance < 50)
        {
          this.showYellow = true
        }
        else if(this.item.distance >= 50 && this.item.distance < 100)
        {
          this.showOrange = true
        }
        else
        {
          this.showRed = true
        }
      }

      if(data2.userStats.unratedSwapCount > 2 || data2.userStats.unacceptedSwapCount > 5)
      {
        this.showSwap = false
      }
     }).catch((error)=>{
      console.log("Promise rejected with " + JSON.stringify(error));
    });

    
  }).catch((error)=>{
    console.log("Promise rejected with " + JSON.stringify(error));
  });  
  console.log('In view item')
  console.log(this.item)
 
  }

  ngOnDestroy() {
    this.subscriptionItem.unsubscribe()
  }

  onProposeSwap() {
    this.router.navigate(['/ProposeSwap']);
  }

  onBack() {
    this.router.navigate(['/SearchItem']);
  }
}
