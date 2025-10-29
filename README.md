# FloodIt Game

A Java implementation of the classic FloodIt puzzle game where players try to flood the entire board with the same color in as few moves as possible.

## Language & Libraries

- **Language**: Java
- **Libraries**: 
  - `java.awt.*` - Graphics and GUI components
  - `javax.swing.*` - Swing GUI framework
  - `java.util.*` - Collections and utilities
- **No External Dependencies**: Uses only standard Java libraries

## Game Files

### Main Implementations
- **`FloodItFixed.java`** - Complete implementation with proper class structure
- **`FloodItSimple.java`** - Simplified, clean version
- **`FloodIt.java`** - Original implementation (requires external libraries)

### Recommended Version
Use **`FloodItFixed.java`** as it has the most complete implementation with:
- Proper flood fill algorithm
- Smooth rendering without flickering
- Move tracking and win/lose conditions
- Clean UI without borders

## How to Run

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- No additional libraries required

### Compilation & Execution
```bash
# Compile the game
javac FloodItFixed.java

# Run the game
java FloodItFixed
```

## Game Rules

1. **Objective**: Flood the entire board with the same color in as few moves as possible
2. **Starting Point**: The flood always starts from the top-left corner
3. **Gameplay**: Click on any color to change the flood color
4. **Flooding**: The flood spreads to adjacent cells that have the same color
5. **Win Condition**: All cells must be the same color
6. **Lose Condition**: Run out of moves before achieving the goal

## Game Features

- **Interactive GUI**: Click on colors to change the flood
- **Move Counter**: Tracks remaining moves
- **Game Status**: Shows win/lose messages
- **Restart Functionality**: Click after game over to start a new game
- **Smooth Rendering**: No flickering or visual glitches
- **Multiple Board Sizes**: Configurable board dimensions

## Technical Details

### Architecture
- **MVC Pattern**: Separates game logic from presentation
- **Object-Oriented Design**: Uses interfaces and classes for cells
- **Event-Driven**: Mouse click handling for user interaction

### Key Classes
- `FloodItWorld` - Main game controller
- `GamePanel` - Custom JPanel for rendering
- `Cell` - Represents individual board cells
- `ICell` - Interface for cell types
- `MtCell` - Empty cell implementation

### Performance Optimizations
- Double buffering for smooth rendering
- Efficient flood fill algorithm
- Optimized repaint regions

## Customization

You can easily modify:
- **Board Size**: Change `sizeOfBoard` variable
- **Number of Colors**: Modify `numColors` variable
- **Move Limit**: Adjust `maxMoves` calculation
- **Cell Size**: Change `Cell.size` static variable
- **Colors**: Add/remove colors in the `colors` ArrayList

## Development Notes

This implementation was created to fix issues in the original FloodIt code:
- Fixed flood fill algorithm
- Removed external library dependencies
- Eliminated screen flickering
- Added proper move tracking
- Implemented smooth rendering
