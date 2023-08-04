import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewacceptrejectitemComponent } from './viewacceptrejectitem.component';

describe('ViewacceptrejectitemComponent', () => {
  let component: ViewacceptrejectitemComponent;
  let fixture: ComponentFixture<ViewacceptrejectitemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ViewacceptrejectitemComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewacceptrejectitemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
