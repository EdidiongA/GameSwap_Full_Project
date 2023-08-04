import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListtemComponent } from './listtem.component';

describe('ListtemComponent', () => {
  let component: ListtemComponent;
  let fixture: ComponentFixture<ListtemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ListtemComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ListtemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
