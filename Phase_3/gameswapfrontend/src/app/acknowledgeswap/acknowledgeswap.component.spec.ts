import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AcknowledgeswapComponent } from './acknowledgeswap.component';

describe('AcknowledgeswapComponent', () => {
  let component: AcknowledgeswapComponent;
  let fixture: ComponentFixture<AcknowledgeswapComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AcknowledgeswapComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AcknowledgeswapComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
