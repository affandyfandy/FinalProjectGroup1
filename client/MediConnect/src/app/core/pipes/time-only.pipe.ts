import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'timeOnly',
  standalone: true,
})
export class TimeOnlyPipe implements PipeTransform {
  transform(value: string): string {
    if (!value) return '';

    // Assuming value is in the format HH:mm:ss or HH:mm:ss AM/PM
    const timeParts = value.split(':');
    return `${timeParts[0]}:${timeParts[1]}`;
  }
}
