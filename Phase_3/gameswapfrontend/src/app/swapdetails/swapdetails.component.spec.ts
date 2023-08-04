import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SwapdetailsComponent } from './swapdetails.component';

describe('SwapdetailsComponent', () => {
  let component: SwapdetailsComponent;
  let fixture: ComponentFixture<SwapdetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SwapdetailsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SwapdetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
