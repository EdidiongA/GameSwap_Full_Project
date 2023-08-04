import { TestBed } from '@angular/core/testing';

import { GameswapService } from './gameswap.service';

describe('GameswapService', () => {
  let service: GameswapService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(GameswapService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
