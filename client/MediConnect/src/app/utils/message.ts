// messages.ts (or constants.ts)

// General Messages
export const MessageSuccess: string = 'Operation completed successfully.';
export const MessageError: string = 'An error occurred. Please try again.';
export const MessageLoading: string = 'Loading, please wait...';
export const MessageNoData: string = 'No data available.';
export const MessageErrorRequiredField: string = 'Field is required.';

// Authentication & Authorization
export const MessageInvalidLogin: string = 'Invalid email or password.';
export const MessageInvalidValidation: string = 'Validation error occurred.';
export const MessageAccessDenied: string =
  'You do not have permission to access this page.';
export const MessageSessionExpired: string =
  'Your session has expired. Please log in again.';

export const MessageValidLogin: string = 'Login Successfully';
export const MessageValidRegister: string = 'Register Successfully';

// User Management
export const MessageUserCreated: string = 'User has been created successfully.';
export const MessageUserUpdated: string =
  'User information updated successfully.';
export const MessageUserDeleted: string = 'User has been deleted.';
export const MessageUserNotFound: string = 'User not found.';
export const MessagePasswordReset: string =
  'Password has been reset successfully.';

// Form Validation
export const MessageFieldRequired: string = 'This field is required.';
export const MessageInvalidEmail: string =
  'Please enter a valid email address.';
export const MessagePasswordTooShort: string =
  'Password must be at least 8 characters.';
export const MessagePasswordsDoNotMatch: string = 'Passwords do not match.';

// CRUD Operations
export const MessageItemCreated: string = 'Item created successfully.';
export const MessageItemUpdated: string = 'Item updated successfully.';
export const MessageItemDeleted: string = 'Item deleted successfully.';
export const MessageItemNotFound: string = 'Item not found.';

// Confirmation Messages
export const MessageConfirmDelete: string =
  'Are you sure you want to delete this item? This action cannot be undone.';
export const MessageConfirmLogout: string = 'Are you sure you want to log out?';
export const MessageConfirmDeactivateUser: string =
  'Are you sure you want to deactivate this user?';

// File Upload
export const MessageFileUploadSuccess: string = 'File uploaded successfully.';
export const MessageFileUploadError: string =
  'Error uploading file. Please try again.';
export const MessageFileSizeExceeded: string =
  'File size exceeds the maximum allowed limit.';

export const MessageValidationPasswordSize: string =
  'Password must be at least 12 characters long.';
export const MessageValidationPasswordPattern: string =
  ' Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character.';

export const MessageValidationNIKPattern: string =
  'NIK must be numbers and exactly 16 digits.';
export const MessageValidationFullnamePattern: string =
  'Fullname can only contain letters and spaces.';
export const MessageValidationEmailPattern: string =
  'Enter a valid email address.';
