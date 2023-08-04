import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { GameswapService } from '../gameswap.service';
import { GameSwapItem } from '../models';
import { Subscription } from 'rxjs';

export interface ItemCount{
  boardGame: number,
  cardGame: number,
  computerGame: number,
  jigsawPuzzle: number,
  videoGame: number,
  total: number
}

export interface ItemsDetail{
  itemNumber: string
  gameType: string
  title: string
  condition: string
  description: string
  details: string
}



@Component({
  selector: 'app-myitem',
  templateUrl: './myitem.component.html',
  styleUrls: ['./myitem.component.scss']
})

export class MyitemComponent implements OnInit {
  
  itemCountDatasource
  itemsDetailDatasource
  itemCount_DATA: ItemCount[] = []

  itemsDetail_DATA: ItemsDetail[] = []

  itemCountColumns: string[] = [
    "boardGame", "cardGame", "computerGame", "jigsawPuzzle", "videoGame","total"];

  itemsDetailColumns: string[] = [
    "itemNumber", "gameType", "title", "condition", "description","details" ];

    
  item: GameSwapItem
  subscriptionItem: Subscription;

  constructor(private router: Router,
    private gameswapService: GameswapService) { }

  ngOnInit(): void {       
    this.subscriptionItem = this.gameswapService.currentMyItemForViewDetails.subscribe(item => this.item = item)
    
    const promise1 = this.gameswapService.getUserOwnSummary()
    promise1.then((data)=>{
      console.log(JSON.stringify(data));     
      this.itemCount_DATA = [ 
        {
          boardGame: data.boardGamesCount, 
          cardGame: data.cardGamesCount,
          computerGame: data.computerGamesCount,
          jigsawPuzzle: data.jigsawPuzzlesCount, 
          videoGame: data.videoGamesCount,
          total: data.totalCount
        }];
        
      this.itemCountDatasource= this.itemCount_DATA;
    }).catch((error)=>{
      console.log("Promise rejected with " + JSON.stringify(error));
    }); 

    const promise2 = this.gameswapService.getUserOwnItem()
    promise2.then((data)=>{
      console.log(JSON.stringify(data));  
      
      data.forEach(element => {
        this.itemsDetail_DATA.push( 
          {
            itemNumber: element.id, 
            gameType: element.gameType, 
            title: element.name, 
            condition: element.condition, 
            description: element.description,
            details:"Detail"
          });
      });
      
     
        
      this.itemsDetailDatasource= this.itemsDetail_DATA;
    }).catch((error)=>{
      console.log("Promise rejected with " + JSON.stringify(error));
    });     
    
    this.shortenDescription();


  }

  onItemDetails(curItem){
    let item : GameSwapItem = {
      itemId: curItem.itemNumber,
      type: curItem.gameType, 
      title: curItem.name,
      condition: curItem.condition, 
      description: curItem.description,
      distance: 0,
      details: '',
      selected: false
    }
    this.gameswapService.updateSelectedMyItemForViewDetails(item);
    //open view item details here.
    this.router.navigate(["/ViewMyItemDetails"]);
  }

  shortenDescription(){ 
    //shorten the displayed description if longer than 100xters.
    let size: number = 100;
    for(let cnt:number = 0;cnt < this.itemsDetail_DATA.length; cnt++){
      if(this.itemsDetail_DATA[cnt].description.length > size){
        this.itemsDetail_DATA[cnt].description = 
            this.itemsDetail_DATA[cnt].description.substring(0, size)+ "...";
      }
    }
  }

  onBackToMain() {
    this.router.navigate(['/Welcome']);
  }
}
