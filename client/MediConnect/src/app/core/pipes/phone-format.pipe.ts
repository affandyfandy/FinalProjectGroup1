import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'phoneFormat',
  standalone: true,
})
export class PhoneFormatPipe implements PipeTransform {
  transform(value: string): string {
    if (!value) return '';

    // Assuming the phone number starts with 0 and needs to be formatted with +62
    if (value.startsWith('0')) {
      return `(+62)${value.substring(1)}`;
    }

    return value; // If the phone number doesn't start with 0, return it unchanged
  }
}
