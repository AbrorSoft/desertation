import { Component, Input, forwardRef } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR, AbstractControl } from '@angular/forms';

@Component({
  selector: 'app-multi-select',
  templateUrl: 'custom-multi-select.component.html',
  styleUrls: ['custom-multi-select.component.scss'],
  standalone: true,
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => CustomMultiSelectComponent),
      multi: true,
    },
  ],
})
export class CustomMultiSelectComponent implements ControlValueAccessor {
  @Input() options: any[] = [];
  @Input() compareWith: (o1: any, o2: any) => boolean = (o1, o2) => o1 === o2;
  @Input() control: AbstractControl | null = null; // Changed from FormControl to AbstractControl

  selectedItems: any[] = [];
  isOpen = false;
  disabled = false;

  private onChange: (value: any) => void = () => {};
  private onTouched: () => void = () => {};

  toggleDropdown(): void {
    if (!this.disabled) {
      this.isOpen = !this.isOpen;
    }
  }

  isSelected(option: any): boolean {
    return this.selectedItems.some(item => this.compareWith(item, option));
  }

  toggleSelection(option: any): void {
    const index = this.selectedItems.findIndex(item => this.compareWith(item, option));
    if (index >= 0) {
      this.selectedItems.splice(index, 1);
    } else {
      this.selectedItems.push(option);
    }
    this.onChange(this.selectedItems);
    this.onTouched();
  }

  removeItem(item: any): void {
    const index = this.selectedItems.findIndex(selected => this.compareWith(selected, item));
    if (index >= 0) {
      this.selectedItems.splice(index, 1);
      this.onChange(this.selectedItems);
      this.onTouched();
    }
  }

  writeValue(value: any): void {
    this.selectedItems = Array.isArray(value) ? value : [];
  }

  registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

  setDisabledState(isDisabled: boolean): void {
    this.disabled = isDisabled;
  }
}
