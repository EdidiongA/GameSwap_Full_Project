export interface GameSwapItem {
    itemId: number;
    type: string;
    title: string;
    titleClass?: string;
    condition: string;
    description: string;
    descriptionClass?: string;
    fullDescription?: string;
    distance: number;  
    details: string;
    selected: boolean;
  }

  export interface SwapItem {
    swapId: number;
    date: string;
    desiredItemId: string;
    desiredItemTitle: string;
    proposer:string;
    rating:number;
    distance:number;
    proposedItemId:number;
    proposedItemTittle:string;
  }