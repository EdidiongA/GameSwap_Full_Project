import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SwaphistoryComponent } from './swaphistory.component';

describe('SwaphistoryComponent', () => {
  let component: SwaphistoryComponent;
  let fixture: ComponentFixture<SwaphistoryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SwaphistoryComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SwaphistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
