import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TrendDisplayComponent } from './trend-display.component';

describe('TrendDisplayComponent', () => {
  let component: TrendDisplayComponent;
  let fixture: ComponentFixture<TrendDisplayComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TrendDisplayComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TrendDisplayComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
