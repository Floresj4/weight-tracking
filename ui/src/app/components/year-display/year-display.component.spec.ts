import { ComponentFixture, TestBed } from '@angular/core/testing';

import { YearDisplayComponent } from './year-display.component';

describe('YearDisplayComponent', () => {
  let component: YearDisplayComponent;
  let fixture: ComponentFixture<YearDisplayComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ YearDisplayComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(YearDisplayComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
