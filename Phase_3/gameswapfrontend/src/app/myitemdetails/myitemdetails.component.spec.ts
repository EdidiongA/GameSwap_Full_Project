import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MyitemdetailsComponent } from './myitemdetails.component';

describe('MyitemdetailsComponent', () => {
  let component: MyitemdetailsComponent;
  let fixture: ComponentFixture<MyitemdetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MyitemdetailsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MyitemdetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
