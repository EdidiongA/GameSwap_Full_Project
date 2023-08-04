import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProposeswapComponent } from './proposeswap.component';

describe('ProposeswapComponent', () => {
  let component: ProposeswapComponent;
  let fixture: ComponentFixture<ProposeswapComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ProposeswapComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ProposeswapComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
