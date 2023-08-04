import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RateswapComponent } from './rateswap.component';

describe('RateswapComponent', () => {
  let component: RateswapComponent;
  let fixture: ComponentFixture<RateswapComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RateswapComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RateswapComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
